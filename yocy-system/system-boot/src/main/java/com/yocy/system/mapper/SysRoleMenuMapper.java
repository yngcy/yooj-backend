package com.yocy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yocy.system.model.bo.RolePermsBO;
import com.yocy.system.model.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Mapper
* @createDate 2024-04-20 00:27:16
* @Entity generator.domain.SysRoleMenu
*/
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId
     * @return
     */
    List<Long> listMenuIdsByRoleId(Long roleId);

    /**
     * 获取权限和拥有权限的角色列表
     */
    List<RolePermsBO> getRolePermsList(String roleCode);
}




