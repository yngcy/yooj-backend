package com.yocy.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.admin.model.entity.SysUserRole;

/**
* @author 25055
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service
* @createDate 2024-04-20 00:27:23
*/
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 判断角色是否存在绑定用户
     * @param roleId
     * @return
     */
    boolean hasAssignedUser(Long roleId);
}
