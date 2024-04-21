package com.yocy.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.admin.model.entity.SysRoleMenu;

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
}
