package com.yocy.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.admin.converter.MenuConverter;
import com.yocy.admin.enums.MenuTypeEnum;
import com.yocy.admin.mapper.SysMenuMapper;
import com.yocy.admin.model.bo.RouteBO;
import com.yocy.admin.model.entity.SysMenu;
import com.yocy.admin.model.form.MenuForm;
import com.yocy.admin.model.query.MenuQuery;
import com.yocy.admin.model.vo.MenuVO;
import com.yocy.admin.model.vo.RouteVO;
import com.yocy.admin.service.SysMenuService;
import com.yocy.admin.service.SysRoleMenuService;
import com.yocy.common.constant.SystemConstants;
import com.yocy.common.enums.StatusEnum;
import com.yocy.common.web.model.Option;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 25055
* @description 针对表【sys_menu(菜单管理)】的数据库操作Service实现
* @createDate 2024-04-20 00:27:11
*/
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService {

    private final MenuConverter menuConverter;

    private final SysRoleMenuService roleMenuService;

    @Override
    public List<MenuVO> listMenus(MenuQuery queryParam) {
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>()
                .like(StrUtil.isNotBlank(queryParam.getKeywords()), SysMenu::getName, queryParam.getKeywords())
                .orderByAsc(SysMenu::getSort));

        Set<Long> parentIds = menuList.stream()
                .map(SysMenu::getParentId)
                .collect(Collectors.toSet());

        Set<Long> menuIds = menuList.stream()
                .map(SysMenu::getId)
                .collect(Collectors.toSet());

        // 获取根节点ID
        List<Long> rootIds = CollectionUtil.subtractToList(parentIds, menuIds);

        // 使用递归构造菜单树
        return rootIds.stream()
                .flatMap(rootId -> buildMenuTree(rootId, menuList).stream())
                .collect(Collectors.toList());
    }

    /**
     * 递归生成菜单列表
     * @param parentId 父菜单ID
     * @param menuList 菜单列表
     * @return 菜单列表
     */
    private List<MenuVO> buildMenuTree(Long parentId, List<SysMenu> menuList) {
        return CollectionUtil.emptyIfNull(menuList)
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(entity -> {
                    MenuVO menuVO = menuConverter.entity2VO(entity);
                    List<MenuVO> children = buildMenuTree(entity.getId(), menuList);
                    menuVO.setChildren(children);
                    return menuVO;
                }).toList();
    }

    @Override
    public List<Option> listMenuOptions() {
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort));
        return buildMenuOptions(SystemConstants.ROOT_NODE_ID, menuList);
    }

    /**
     * 递归生成树形选择
     * @param parentId
     * @param menuList
     * @return
     */
    private List<Option> buildMenuOptions(Long parentId, List<SysMenu> menuList) {
        List<Option> menuOptions = new ArrayList<>();

        for (SysMenu menu : menuList) {
            if (menu.getParentId().equals(parentId)) {
                Option option = new Option(menu.getId(), menu.getName());
                List<Option> subMenuOption = buildMenuOptions(menu.getId(), menuList);
                if (!subMenuOption.isEmpty()) {
                    option.setChildren(subMenuOption);
                }
                menuOptions.add(option);
            }
        }

        return menuOptions;
    }

    @Override
    public boolean saveMenu(MenuForm menuForm) {
        String path = menuForm.getPath();
        MenuTypeEnum menuType = menuForm.getType();

        // 如果是目录
        if (menuType == MenuTypeEnum.CATALOG) {
            if (menuForm.getParentId().equals(0L) && !path.startsWith("/")) {
                menuForm.setPath("/" + path);
            }
            menuForm.setComponent("Layout");
        }
        // 如果是外链
        else if (menuType == MenuTypeEnum.EXTLINK) {
            menuForm.setComponent(null);
        }

        SysMenu entity = menuConverter.form2Entity(menuForm);
        String treePath = generateMenuTreePath(menuForm.getParentId());
        entity.setTreePath(treePath);

        boolean result = this.saveOrUpdate(entity);
        if (result) {
            // 编辑刷新角色缓存
            if (menuForm.getId() != null) {
                roleMenuService.refreshRolePermsCache();
            }
        }
        return result;
    }

    /**
     * 生成菜单树路径
     * @param parentId
     * @return
     */
    private String generateMenuTreePath(Long parentId) {
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            return String.valueOf(parentId);
        } else {
            SysMenu parent = this.getById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }

    @Override
    public List<RouteVO> listRoutes() {
        List<RouteBO> menuList = this.baseMapper.listRoutes();

        return buildRoutes(SystemConstants.ROOT_NODE_ID, menuList);
    }

    /**
     * 递归生成树形路由
     * @param parentId
     * @param menuList
     * @return
     */
    private List<RouteVO> buildRoutes(Long parentId, List<RouteBO> menuList) {
        List<RouteVO> routeList = new ArrayList<>();

        for (RouteBO menu : menuList) {
            if (menu.getId().equals(parentId)) {
                RouteVO routeVO = toRouteVO(menu);
                List<RouteVO> children = buildRoutes(menu.getId(), menuList);
                if (!children.isEmpty()) {
                    routeVO.setChildren(children);
                }
                routeList.add(routeVO);
            }
        }

        return routeList;
    }

    /**
     * 根据RouteBO创建RouteVO
     * @param routeBO
     * @return
     */
    private RouteVO toRouteVO(RouteBO routeBO) {
        RouteVO routeVO = new RouteVO();

        // 路由name需要驼峰，首字母大写
        String routeName = StringUtils.capitalize(StrUtil.toCamelCase(routeBO.getPath(),'-'));
        routeVO.setName(routeName);
        routeVO.setPath(routeBO.getPath());
        routeVO.setComponent(routeBO.getComponent());
        routeVO.setRedirect(routeBO.getRedirect());

        RouteVO.Meta meta = new RouteVO.Meta();
        meta.setTitle(routeBO.getName());
        meta.setIcon(routeBO.getIcon());
        meta.setHidden(StatusEnum.DISABLE.getValue().equals(routeBO.getVisible()));
        meta.setRoles(routeBO.getRoles());
        // 【菜单】是否开启页面缓存
        if (MenuTypeEnum.MENU.equals(routeBO.getType()) && ObjectUtil.equals(routeBO.getAlwaysShow(), 1)) {
            meta.setKeepAlive(true);
        }
        // 【目录】只有一个路由是否始终显示
        if (MenuTypeEnum.CATALOG.equals(routeBO.getType()) && ObjectUtil.equals(routeBO.getAlwaysShow(), 1)) {
            meta.setAlwaysShow(true);
        }

        routeVO.setMeta(meta);

        return routeVO;
    }

    @Override
    public boolean updateMenuVisable(Long menuId, Integer visible) {
        return this.update(new LambdaUpdateWrapper<SysMenu>()
                .eq(SysMenu::getId, menuId)
                .set(SysMenu::getVisible, visible));
    }

    @Override
    public MenuForm getMenuForm(Long id) {
        SysMenu entity = this.getById(id);
        return menuConverter.entity2Form(entity);
    }

    @Override
    public boolean deleteMenu(Long id) {
        boolean result = this.remove(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getId, id)
                .or()
                .apply("CONCAT (',',tree_path,',') LIKE CONCAT ('%,',{0},',%'", id));
        // 刷新角色权限缓存
        if (result) {
            roleMenuService.refreshRolePermsCache();
        }
        return result;
    }
}




