package com.yocy.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yocy.common.base.BaseEntity;
import lombok.Data;

/**
 * 角色表
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
public class SysRole extends BaseEntity {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 角色状态(1-正常；0-停用)
     */
    private Integer status;

    /**
     * 逻辑删除标识(0-未删除；1-已删除)
     */
    private Integer deleted;

    /**
     * 数据权限(0-所有数据；1-部门及子部门数据；2-本部门数据；3-本人数据)
     */
    private Integer dataScope;

}