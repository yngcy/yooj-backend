package com.yocy.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.system.model.form.RoleForm;
import com.yocy.system.model.query.RolePageQuery;
import com.yocy.system.model.vo.RolePageVO;
import com.yocy.system.service.SysRoleService;
import com.yocy.common.result.PageResult;
import com.yocy.common.result.Result;
import com.yocy.common.web.annotation.PreventDuplicateResubmit;
import com.yocy.common.web.model.Option;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Tag(name = "角色接口")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "角色分页列表")
    @GetMapping("/page")
    public PageResult<RolePageVO> getRolePage(@ParameterObject RolePageQuery queryParams) {
        Page<RolePageVO> rolePage = roleService.getRolePage(queryParams);
        return PageResult.success(rolePage);
    }

    @Operation(summary = "角色下拉选项")
    @GetMapping("/options")
    public Result<List<Option>> listRoleOptions() {
        List<Option> roleOptions = roleService.listRoleOptions();
        return Result.success(roleOptions);
    }

    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:role:add')")
    @PreventDuplicateResubmit
    public Result addRole(@Valid @RequestBody RoleForm roleForm) {
        boolean result = roleService.saveRole(roleForm);
        return Result.judge(result);
    }

    @Operation(summary = "角色表单数据")
    @GetMapping("/{roleId}/form")
    public Result<RoleForm> getRoleForm(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        RoleForm roleForm = roleService.getRoleForm(roleId);
        return Result.success(roleForm);
    }

    @Operation(summary = "修改角色")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('sys:role:edit')")
    public Result updateRole(@Parameter(description = "角色ID") @PathVariable Long id, @Valid @RequestBody RoleForm roleForm) {
        boolean result = roleService.saveRole(roleForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:role:delete')")
    public Result deleteRoles(@Parameter(description = "删除角色，多个用英文逗号(,)分隔") @PathVariable String ids) {
        boolean result = roleService.deleteRoles(ids);
        return Result.judge(result);
    }

    @Operation(summary = "修改角色状态")
    @PutMapping(value = "/{roleId}/status")
    public Result updateRoleStatus(
            @Parameter(description = "角色ID") @PathVariable Long roleId,
            @Parameter(description = "状态(1:正常;0:禁用)") Integer status) {
        boolean result = roleService.updateRoleStatus(roleId, status);
        return Result.judge(result);
    }

    @Operation(summary = "角色菜单ID集合")
    @GetMapping("/{roleId}/menuIds")
    public Result<List<Long>> getRoleMenuIds(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<Long> menuIds = roleService.getRoleMenuIds(roleId);
        return Result.success(menuIds);
    }

    @Operation(summary = "分配菜单权限给角色")
    @PutMapping("/{roleId}/menus")
    public Result assignMenusToRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> menuIds) {
        boolean result = roleService.assignMenusToRole(roleId, menuIds);
        return Result.judge(result);
    }
}
