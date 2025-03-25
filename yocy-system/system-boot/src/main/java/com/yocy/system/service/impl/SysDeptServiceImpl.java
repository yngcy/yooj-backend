package com.yocy.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.system.converter.DeptConverter;
import com.yocy.system.mapper.SysDeptMapper;
import com.yocy.system.model.entity.SysDept;
import com.yocy.system.model.form.DeptForm;
import com.yocy.system.model.query.DeptQuery;
import com.yocy.system.model.vo.DeptVO;
import com.yocy.system.service.SysDeptService;
import com.yocy.common.constant.SystemConstants;
import com.yocy.common.enums.StatusEnum;
import com.yocy.common.web.model.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 25055
* @description 针对表【sys_dept(部门表)】的数据库操作Service实现
* @createDate 2024-04-20 00:25:38
*/
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept>
    implements SysDeptService {

    private final DeptConverter deptConverter;

    @Override
    public List<DeptVO> listDepartments(DeptQuery queryParams) {
        // 查询参数
        String keywords = queryParams.getKeywords();
        Integer status = queryParams.getStatus();

        // 查询数据
        List<SysDept> deptList = this.list(
                new LambdaQueryWrapper<SysDept>()
                        .like(StrUtil.isNotBlank(keywords), SysDept::getName, keywords)
                        .eq(status != null, SysDept::getStatus, status)
                        .orderByAsc(SysDept::getSort)
        );

        Set<Long> deptIds = deptList.stream()
                .map(SysDept::getId)
                .collect(Collectors.toSet());

        Set<Long> parentIds = deptList.stream()
                .map(SysDept::getParentId)
                .collect(Collectors.toSet());

        List<Long> rootIds = CollectionUtil.subtractToList(parentIds, deptIds);

        List<DeptVO> deptVOList = new ArrayList<>();
        for (Long rootId : rootIds) {
            deptVOList.addAll(recurDeptList(rootId, deptList));
        }
        return deptVOList;
    }

    /**
     * 递归生成部门树形列表
     * @param parentId
     * @param deptList
     * @return
     */
    public List<DeptVO> recurDeptList(Long parentId, List<SysDept> deptList) {
        return deptList.stream()
                .filter(dept -> dept.getParentId().equals(parentId))
                .map(dept -> {
                    DeptVO deptVO = deptConverter.entity2VO(dept);
                    List<DeptVO> children = recurDeptList(dept.getId(), deptList);
                    deptVO.setChildren(children);
                    return deptVO;
                }).collect(Collectors.toList());
    }


    /**
     * 获取部门下拉选项列表
     * @return
     */
    @Override
    public List<Option> listDeptOptions() {

        List<SysDept> deptList = this.list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getStatus, StatusEnum.ENABLE.getValue())
                .select(SysDept::getId, SysDept::getParentId, SysDept::getName)
                .orderByAsc(SysDept::getSort));

        Set<Long> parentIds = deptList.stream()
                .map(SysDept::getParentId)
                .collect(Collectors.toSet());

        Set<Long> deptIds = deptList.stream()
                .map(SysDept::getId)
                .collect(Collectors.toSet());

        List<Long> rootIds = CollectionUtil.subtractToList(parentIds, deptIds);

        List<Option> options = new ArrayList<>();
        for (Long rootId : rootIds) {
            options.addAll(recurDeptTreeOption(rootId, deptList));
        }
        return options;
    }

    @Override
    public Long saveDept(DeptForm formData) {
        SysDept entity = deptConverter.form2Entity(formData);
        // 部门路径
        String treePath = generateDeptTreePath(formData.getParentId());
        entity.setTreePath(treePath);
        // 保存部门并返回id
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateDept(Long deptId, DeptForm formData) {
        SysDept entity = deptConverter.form2Entity(formData);
        entity.setId(deptId);
        // 部门路径
        String treePath = generateDeptTreePath(formData.getParentId());
        entity.setTreePath(treePath);
        // 保存部门并返回id
        this.updateById(entity);
        return entity.getId();
    }

    @Override
    public boolean deleteByIds(String ids) {
        // 删除部门以及子部门
        if (StrUtil.isNotBlank(ids)) {
            String[] deptIds = ids.split(",");
            for (String deptId : deptIds) {
                this.remove(new LambdaQueryWrapper<SysDept>()
                        .eq(SysDept::getId, deptId)
                        .or()
                        .apply("CONCAT (',',tree_path,',') LIKE CONCAT ('%,',{0},',%')", deptIds));
            }
        }
        return true;
    }

    @Override
    public DeptForm getDeptForm(Long deptId) {
        SysDept entity = this.getOne(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getId, deptId)
                .select(
                        SysDept::getId,
                        SysDept::getName,
                        SysDept::getParentId,
                        SysDept::getStatus,
                        SysDept::getSort
                ));
        return deptConverter.entity2Form(entity);
    }

    /**
     * 部门路径生成
     * @param parentId
     * @return 返回路径字符串，用英文逗号分隔，eg: 1,2,3
     */
    public String generateDeptTreePath(Long parentId) {
        String treePath = null;
        // 如果是根，则直接返回根
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            treePath = String.valueOf(parentId);
        } else {
            // 存在父节点，则获取其 treePath，然后将当前节点加入 treePath
            SysDept parent = this.getById(parentId);
            if (parent != null) {
                treePath = parent.getTreePath() + "," + parent.getId();
            }
        }
        return treePath;
    }


    public static List<Option> recurDeptTreeOption(Long parentId, List<SysDept> deptList) {
        List<Option> options = CollectionUtil.emptyIfNull(deptList).stream()
                .filter(dept -> dept.getParentId().equals(parentId))
                .map(dept -> {
                    Option option = new Option(dept.getId(), dept.getName());
                    List<Option> children = recurDeptTreeOption(dept.getId(), deptList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        option.setChildren(children);
                    }
                    return option;
                }).collect(Collectors.toList());
        return options;
    }
}




