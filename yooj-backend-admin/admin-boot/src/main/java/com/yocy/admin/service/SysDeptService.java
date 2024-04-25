package com.yocy.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.admin.model.entity.SysDept;
import com.yocy.admin.model.form.DeptForm;
import com.yocy.admin.model.query.DeptQuery;
import com.yocy.admin.model.vo.DeptVO;
import com.yocy.common.web.model.Option;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_dept(部门表)】的数据库操作Service
* @createDate 2024-04-20 00:25:38
*/
public interface SysDeptService extends IService<SysDept> {

    /**
     * 获取部门列表
     * @param queryParams
     * @return
     */
    List<DeptVO> listDepartments(DeptQuery queryParams);

    /**
     * 获取树形下拉选项
     * @return
     */
    List<Option> listDeptOptions();

    /**
     * 新增部门
     * @param formData
     * @return
     */
    Long saveDept(DeptForm formData);

    /**
     * 修改部门
     * @param deptId
     * @param deptForm
     * @return
     */
    Long updateDept(Long deptId, DeptForm deptForm);

    /**
     * 删除部门
     * @param ids 部门id，支持多个，用英文逗号分隔，
     * @return
     */
    boolean deleteByIds(String ids);

    /**
     * 获取部门表单
     * @param deptId
     * @return
     */
    DeptForm getDeptForm(Long deptId);

}
