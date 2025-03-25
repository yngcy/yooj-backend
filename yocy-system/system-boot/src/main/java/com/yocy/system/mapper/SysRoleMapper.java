package com.yocy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yocy.system.model.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
* @author 25055
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-04-20 00:27:14
* @Entity generator.domain.SysRole
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 获取最大范围的数据权限
     *
     * @param roles
     * @return
     */
    Integer getMaxDataRangeDataScope(Set<String> roles);
}




