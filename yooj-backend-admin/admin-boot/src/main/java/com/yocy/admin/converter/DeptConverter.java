package com.yocy.admin.converter;


import com.yocy.admin.model.entity.SysDept;
import com.yocy.admin.model.form.DeptForm;
import com.yocy.admin.model.vo.DeptVO;
import org.mapstruct.Mapper;

/**
 * 部门对象转换器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Mapper(componentModel = "spring")
public interface DeptConverter {

    DeptForm entity2Form(SysDept entity);

    DeptVO entity2VO(SysDept entity);

    SysDept form2Entity(DeptForm deptForm);
}
