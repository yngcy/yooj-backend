package com.yocy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yocy.admin.model.entity.SysUserRole;
import org.mapstruct.Mapper;

/**
* @author 25055
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Mapper
* @createDate 2024-04-20 00:27:23
* @Entity generator.domain.SysUserRole
*/
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 获取角色绑定的用户数
     *
     * @param roleId 角色ID
     */
    int countUsersForRole(Long roleId);
}




