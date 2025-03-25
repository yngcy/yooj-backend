package com.yocy.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yocy.common.base.BaseEntity;
import lombok.Data;

/**
 * 菜单管理
 * @TableName sys_menu
 */
@TableName(value ="sys_menu")
@Data
public class SysMenu extends BaseEntity {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单类型(1：菜单；2：目录；3：外链；4：按钮)
     */
    private Integer type;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由路径(浏览器地址栏路径)
     */
    private String path;

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    private String component;

    /**
     * 按钮权限标识
     */
    private String perm;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态(0:禁用;1:开启)
     */
    private Integer visible;

    /**
     * 跳转路径
     */
    private String redirect;

    /**
     * 
     */
    private String treePath;

    /**
     * 【目录】只有一个子路由是否始终显示(1:是 0:否)
     */
    private Integer alwaysShow;

    /**
     * 【菜单】是否开启页面缓存(1:是 0:否)
     */
    private Integer keepAlive;

}