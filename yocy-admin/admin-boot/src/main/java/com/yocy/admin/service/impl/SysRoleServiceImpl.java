package com.yocy.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.admin.converter.RoleConverter;
import com.yocy.admin.mapper.SysRoleMapper;
import com.yocy.admin.model.entity.SysRole;
import com.yocy.admin.model.entity.SysRoleMenu;
import com.yocy.admin.model.form.RoleForm;
import com.yocy.admin.model.query.RolePageQuery;
import com.yocy.admin.model.vo.RolePageVO;
import com.yocy.admin.service.SysRoleMenuService;
import com.yocy.admin.service.SysRoleService;
import com.yocy.admin.service.SysUserRoleService;
import com.yocy.common.constant.SystemConstants;
import com.yocy.common.security.utils.SecurityUtils;
import com.yocy.common.web.model.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 25055
 * @description 针对表【sys_role(角色表)】的数据库操作Service实现
 * @createDate 2024-04-20 00:27:14
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    private final SysRoleMenuService roleMenuService;

    private final SysUserRoleService userRoleService;

    private final RoleConverter roleConverter;

    @Override
    public Page<RolePageVO> getRolePage(RolePageQuery queryParams) {
        // 查询参数
        String keywords = queryParams.getKeywords();
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();

        // 查询数据
        Page<SysRole> rolePage = this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysRole>()
                        .and(StrUtil.isNotBlank(keywords),
                                wrapper -> wrapper
                                        .like(StrUtil.isNotBlank(keywords), SysRole::getName, keywords)
                                        .or()
                                        .like(StrUtil.isNotBlank(keywords), SysRole::getCode, keywords))
                        // 非超级管理员不显示超级管理员角色
                        .ne(SecurityUtils.isRoot(), SysRole::getCode, SystemConstants.ROOT_NODE_ID));
        // 实体转换
        Page<RolePageVO> roleVOPage = roleConverter.entityPage2VOPage(rolePage);
        return roleVOPage;
    }

    @Override
    public List<Option> listRoleOptions() {
        // 查询数据
        List<SysRole> roleList = this.list(new LambdaQueryWrapper<SysRole>()
                .ne(!SecurityUtils.isRoot(), SysRole::getCode, SystemConstants.ROOT_NODE_ID)
                .select(SysRole::getId, SysRole::getName)
                .orderByAsc(SysRole::getSort));
        return roleConverter.entities2Options(roleList);
    }

    @Override
    public boolean saveRole(RoleForm roleForm) {

        Long roleId = roleForm.getId();
        // 编辑角色时，判断角色是否存在
        SysRole oldRole = null;
        if (roleId != null) {
            oldRole = this.getById(roleId);
            Assert.isTrue(oldRole != null, "角色不存在");
        }

        String roleCode = roleForm.getCode();
        long count = this.count(new LambdaQueryWrapper<SysRole>()
                .ne(roleId != null, SysRole::getId, roleId)
                .and(wrapper -> wrapper
                        .eq(SysRole::getCode, roleCode)
                        .or()
                        .eq(SysRole::getName, roleForm.getName())));
        Assert.isTrue(count == 0, "角色名称或角色编码已存在，请修改后重试");
        SysRole role = roleConverter.form2Entity(roleForm);

        boolean result = this.saveOrUpdate(role);
        if (result) {
            // 判断角色编码或状态是否修改，若修改则刷新权限缓存
            if (oldRole != null &&
                    (!StrUtil.equals(oldRole.getCode(), roleCode) ||
                            !ObjectUtil.equals(oldRole.getStatus(), roleForm.getStatus()))) {
                roleMenuService.refreshRolePermsCache(oldRole.getCode(), roleCode);
            }
        }
        return result;
    }

    @Override
    public RoleForm getRoleForm(Long roleId) {

        SysRole entity = this.getById(roleId);
        return roleConverter.entity2Form(entity);
    }

    @Override
    public boolean updateRoleStatus(Long roleId, Integer status) {
        SysRole role = this.getById(roleId);
        Assert.isTrue(role != null, "角色不存在");

        role.setStatus(status);
        boolean result = this.updateById(role);
        if (result) {
            // 刷新角色的权限缓存
            roleMenuService.refreshRolePermsCache(role.getCode());
        }
        return result;
    }

    @Override
    public boolean deleteRoles(String ids) {
        List<Long> roleIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();

        for (Long roleId : roleIds) {
            SysRole role = this.getById(roleId);
            Assert.isTrue(role != null, "角色不存在");

            // 判断角色是否被用户关联
            boolean isRoleAssigned = userRoleService.hasAssignedUser(roleId);
            Assert.isTrue(!isRoleAssigned, "角色【{}】已分配用户，请先解除关联后删除", role.getName());

            boolean result = this.removeById(roleId);
            if (result) {
                // 删除成功，刷新权限缓存
                roleMenuService.refreshRolePermsCache(role.getCode());
            }
        }
        return true;
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        return roleMenuService.listMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    public boolean assignMenusToRole(Long roleId, List<Long> menuIds) {
        SysRole role = this.getById(roleId);
        Assert.isTrue(role != null, "角色不存在");

        // 删除角色菜单
        roleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));
        // 新增角色菜单
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> new SysRoleMenu(roleId, menuId))
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenus);
        }
        // 刷新角色的权限缓存
        roleMenuService.refreshRolePermsCache(role.getCode());
        return true;
    }

    @Override
    public Integer getMaxDataRangeDataScope(Set<String> roles) {
        Integer dataScope = this.baseMapper.getMaxDataRangeDataScope(roles);
        return dataScope;
    }
}




