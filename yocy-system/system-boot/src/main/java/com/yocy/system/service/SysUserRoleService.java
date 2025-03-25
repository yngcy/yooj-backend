package com.yocy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.system.model.entity.SysUserRole;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service
* @createDate 2024-04-20 00:27:23
*/
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 保存用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID集合
     * @return boolean 是否保存成功
     */
    boolean saveUserRoles(Long userId, List<Long> roleIds);

    /**
     * 判断角色是否存在绑定用户
     * @param roleId
     * @return
     */
    boolean hasAssignedUser(Long roleId);
}
