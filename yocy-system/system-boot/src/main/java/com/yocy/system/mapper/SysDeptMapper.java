package com.yocy.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.yocy.system.model.entity.SysDept;
import com.yocy.common.mybatis.annotation.DataPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_dept(部门表)】的数据库操作Mapper
* @createDate 2024-04-20 00:25:38
* @Entity generator.domain.SysDept
*/
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    @DataPermission(deptIdColumnName = "id")
    @Override
    List<SysDept> selectList(@Param(Constants.WRAPPER) Wrapper<SysDept> queryWrapper);
}




