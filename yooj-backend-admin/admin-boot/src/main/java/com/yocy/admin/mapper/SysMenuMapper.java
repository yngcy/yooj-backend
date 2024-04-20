package com.yocy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yocy.admin.model.bo.RouteBO;
import com.yocy.admin.model.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_menu(菜单管理)】的数据库操作Mapper
* @createDate 2024-04-20 00:27:11
* @Entity generator.domain.SysMenu
*/
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<RouteBO> listRoutes();
}




