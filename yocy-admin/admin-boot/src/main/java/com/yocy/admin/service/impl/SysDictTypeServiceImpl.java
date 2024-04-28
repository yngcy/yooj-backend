package com.yocy.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.admin.converter.DictConverter;
import com.yocy.admin.converter.DictTypeConverter;
import com.yocy.admin.mapper.SysDictTypeMapper;
import com.yocy.admin.model.entity.SysDict;
import com.yocy.admin.model.entity.SysDictType;
import com.yocy.admin.model.form.DictTypeForm;
import com.yocy.admin.model.query.DictTypePageQuery;
import com.yocy.admin.model.vo.DictTypePageVO;
import com.yocy.admin.service.SysDictService;
import com.yocy.admin.service.SysDictTypeService;
import com.yocy.common.web.model.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 25055
* @description 针对表【sys_dict_type(字典类型表)】的数据库操作Service实现
* @createDate 2024-04-20 00:27:07
*/
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType>
    implements SysDictTypeService {

    private final SysDictService dictItemService;

    private final DictTypeConverter dictTypeConverter;
    private final DictConverter dictConverter;

    @Override
    public Page<DictTypePageVO> getDictTypePage(DictTypePageQuery queryParams) {
        // 查询参数
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        String keywords = queryParams.getKeywords();

        // 查询数据
        Page<SysDictType> dictTypePage = this.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysDictType>()
                        .like(StrUtil.isNotBlank(keywords), SysDictType::getName, keywords)
                        .or()
                        .like(StrUtil.isNotBlank(keywords), SysDictType::getCode, keywords)
                        .select(
                                SysDictType::getId,
                                SysDictType::getName,
                                SysDictType::getCode,
                                SysDictType::getStatus,
                                SysDictType::getRemark
                        )
        );

        // 实体转换
        Page<DictTypePageVO> pageVO = dictTypeConverter.entityPage2VOPage(dictTypePage);
        return pageVO;
    }

    @Override
    public DictTypeForm getDictTypeForm(Long id) {
        // 获取entity
        SysDictType entity = this.getOne(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getId, id)
                .select(
                        SysDictType::getId,
                        SysDictType::getName,
                        SysDictType::getCode,
                        SysDictType::getStatus,
                        SysDictType::getRemark
                ));
        Assert.isTrue(entity != null, "字典类型不存在");

        // 实体转换
        DictTypeForm dictTypeForm = dictTypeConverter.entity2Form(entity);
        return dictTypeForm;
    }

    @Override
    public boolean saveDictType(DictTypeForm dictTypeForm) {
        SysDictType entity = dictTypeConverter.form2Entity(dictTypeForm);
        boolean result = this.save(entity);
        return result;
    }

    @Override
    public boolean updateDictType(Long id, DictTypeForm dictTypeForm) {
        // 获取字典类型
        SysDictType dictType = this.getById(id);
        Assert.isTrue(dictType != null, "字典类型不存在");

        SysDictType entity = dictTypeConverter.form2Entity(dictTypeForm);
        boolean result = this.updateById(entity);
        // todo 这是一个事务，需要考虑修改失败的问题
        if (result) {
            // 字典类型code变化，同步修改字典项的类型code
            String oldCode = dictType.getCode();
            String newCode = dictTypeForm.getCode();
            if (!StrUtil.equals(oldCode, newCode)) {
                dictItemService.update(new LambdaUpdateWrapper<SysDict>()
                        .eq(SysDict::getTypeCode, oldCode)
                        .set(SysDict::getTypeCode, newCode));
            }
        }
        return result;
    }

    @Override
    public boolean deleteDictTypes(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除数据为空");

        List<String> ids = Arrays.stream(idsStr.split(","))
                .toList();

        // 删除数据字典项
        List<String> dictTypeCodes = this.list(new LambdaQueryWrapper<SysDictType>()
                .in(SysDictType::getId, ids)
                .select(SysDictType::getCode))
                .stream()
                .map(SysDictType::getCode)
                .toList();
        // todo 这也是一个事务，需要考虑删除失败的问题
        if (CollectionUtil.isNotEmpty(dictTypeCodes)) {
            dictItemService.remove(new LambdaQueryWrapper<SysDict>()
                    .in(SysDict::getTypeCode, dictTypeCodes));
        }
        // 删除字典类型
        boolean result = this.removeByIds(ids);

        return result;
    }

    @Override
    public List<Option> listDictItemsByTypeCode(String typeCode) {
        // 数据字典项
        List<SysDict> dictItems = dictItemService.list(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getTypeCode, typeCode)
                .select(SysDict::getValue, SysDict::getName));

        // 实体转换
        List<Option> options = CollectionUtil.emptyIfNull(dictItems)
                .stream()
                .map(dictItem -> new Option(dictItem.getValue(), dictItem.getName()))
                .collect(Collectors.toList());

        return options;
    }
}




