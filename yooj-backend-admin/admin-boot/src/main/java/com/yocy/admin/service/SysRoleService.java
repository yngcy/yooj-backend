package com.yocy.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.admin.model.entity.SysRole;
import com.yocy.admin.model.form.RoleForm;
import com.yocy.admin.model.query.RolePageQuery;
import com.yocy.admin.model.vo.RolePageVO;
import com.yocy.common.web.model.Option;

import java.util.List;
import java.util.Set;

/**
* @author 25055
* @description 针对表【sys_role(角色表)】的数据库操作Service
* @createDate 2024-04-20 00:27:14
*/
public interface SysRoleService extends IService<SysRole> {

    /**
     * 角色分页列表
     * @param queryParams 角色查询参数
     * @return {@link Page<RolePageVO>} - 角色分页列表
     */
    Page<RolePageVO> getRolePage(RolePageQuery queryParams);

    /**
     * 角色下拉列表
     * @return {@link List<Option>} - 角色下拉列表
     */
    List<Option> listRoleOptions();

    /**
     * 新增角色
     * @param roleForm 角色表单数据
     * @return
     */
    boolean saveRole(RoleForm roleForm);

    /**
     * 获取角色表单数据
     * @param roleId 角色ID
     * @return {@link RoleForm}
     */
    RoleForm getRoleForm(Long roleId);

    /**
     * 修改角色状态
     * @param roleId 角色ID
     * @param status 角色状态(1:正常; 0:禁用)
     * @return
     */
    boolean updateRoleStatus(Long roleId, Integer status);

    /**
     * 批量删除角色
     * @param ids 角色ID，多个用英文逗号(,)分隔
     * @return
     */
    boolean deleteRoles(String ids);

    /**
     * 获取角色的菜单集合
     * @param roleId 角色ID
     * @return 菜单ID集合（包括按钮权限ID）
     */
    List<Long> getRoleMenuIds(Long roleId);

    /**
     * 分配角色资源权限
     * @param roleId
     * @param menuIds
     * @return
     */
    boolean assignMenusToRole(Long roleId, List<Long> menuIds);

    /**
     * 获取最大范围的数据权限
     * @param roles 角色编码集合
     * @return 最大的范围数据权限
     */
    Integer getMaxDataRangeScope(Set<String> roles);
}
