package com.yocy.admin.controller;

import com.yocy.admin.model.form.MenuForm;
import com.yocy.admin.model.query.MenuQuery;
import com.yocy.admin.model.vo.MenuVO;
import com.yocy.admin.model.vo.RouteVO;
import com.yocy.admin.service.SysMenuService;
import com.yocy.common.result.Result;
import com.yocy.common.web.annotation.PreventDuplicateResubmit;
import com.yocy.common.web.model.Option;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Tag(name = "菜单接口")
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Slf4j
public class SysMenuController {

    private final SysMenuService menuService;

    @Operation(description = "菜单列表")
    @GetMapping
    public Result<List<MenuVO>> listMenus(@ParameterObject MenuQuery queryParams) {
        List<MenuVO> menuList = menuService.listMenus(queryParams);
        return Result.success(menuList);
    }

    @Operation(description = "菜单下拉列表")
    @GetMapping("/options")
    public Result listMenuOptions() {
        List<Option> menus = menuService.listMenuOptions();
        return Result.success(menus);
    }

    @Operation(description = "路由列表")
    @GetMapping("/routes")
    public Result<List<RouteVO>> listRoutes() {
        List<RouteVO> routeList = menuService.listRoutes();
        return Result.success(routeList);
    }

    @Operation(description = "菜单表单数据")
    @GetMapping("/{id}/form")
    public Result<MenuForm> getMenuForm(@Parameter(description = "菜单ID") @PathVariable Long id) {
        MenuForm menuForm = menuService.getMenuForm(id);
        return Result.success(menuForm);
    }

    @Operation(description = "新增菜单")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:menu:add')")
    @PreventDuplicateResubmit
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    public Result addMenu(@RequestBody MenuForm menuForm) {
        boolean result = menuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(description = "修改菜单")
    @PutMapping("/{id}")
    @PreAuthorize("@ss.hasPerm('sys:menu:edit')")
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    public Result updateMenu(@RequestBody MenuForm menuForm) {
        boolean result = menuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(description = "删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPerm('sys:menu:delete')")
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    public Result deleteMenu(@Parameter(description = "菜单ID，多个以英文逗号(,)分隔") @PathVariable("id") Long id) {
        boolean result = menuService.deleteMenu(id);
        return Result.judge(result);
    }

    @Operation(description = "修改菜单显示状态")
    @PatchMapping("/{menuId}")
    public Result updateMenuVisible(
            @Parameter(description = "菜单ID") @PathVariable Long menuId,
            @Parameter(description = "显示状态(1:显示;0:隐藏)") Integer visible) {
        boolean result = menuService.updateMenuVisable(menuId, visible);
        return Result.judge(result);
    }

}
