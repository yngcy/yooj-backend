package com.yocy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.system.model.entity.SysMenu;
import com.yocy.system.model.form.MenuForm;
import com.yocy.system.model.query.MenuQuery;
import com.yocy.system.model.vo.MenuVO;
import com.yocy.system.model.vo.RouteVO;
import com.yocy.common.web.model.Option;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_menu(菜单管理)】的数据库操作Service
* @createDate 2024-04-20 00:27:11
*/
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单列表
     * @param queryParam
     * @return
     */
    List<MenuVO> listMenus(MenuQuery queryParam);

    /**
     * 获取菜单下拉列表
     * @return
     */
    List<Option> listMenuOptions();

    /**
     * 新增菜单
     * @param menuForm
     * @return
     */
    boolean saveMenu(MenuForm menuForm);

    /**
     * 获取路由列表
     * @return
     */
    List<RouteVO> listRoutes();

    /**
     * 修改菜单显示状态
     * @param menuId 菜单ID
     * @param visible 是否显示（1->显示；0->隐藏）
     * @return
     */
    boolean updateMenuVisable(Long menuId, Integer visible);

    /**
     * 获取菜单表单数据
     * @param id
     * @return
     */
    MenuForm getMenuForm(Long id);

    /**
     * 删除菜单
     * @param id
     * @return
     */
    boolean deleteMenu(Long id);
}
