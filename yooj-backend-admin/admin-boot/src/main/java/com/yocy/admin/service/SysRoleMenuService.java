package com.yocy.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.admin.model.entity.SysRoleMenu;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service
* @createDate 2024-04-20 00:27:16
*/
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 刷新权限缓存(所有角色)
     */
    void refreshRolePermsCache();

    /**
     * 刷新权限缓存（修改角色编码时调用）
     * @param oldRoleCode 修改前的角色编码
     * @param newRoleCode 修改后的角色编码
     */
    void refreshRolePermsCache(String oldRoleCode, String newRoleCode);

    /**
     * 刷新权限缓存（指定角色）
     * @param roleCode
     */
    void refreshRolePermsCache(String roleCode);

    /**
     * 获取角色拥有的菜单ID列表
     * @param roleId
     * @return
     */
    List<Long> listMenuIdsByRoleId(Long roleId);
}
