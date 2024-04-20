package com.yocy.admin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yocy.common.base.BaseEntity;
import lombok.Data;

/**
 * 字典类型表
 * @TableName sys_dict_type
 */
@TableName(value ="sys_dict_type")
@Data
public class SysDictType extends BaseEntity {
    /**
     * 主键 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型编码
     */
    private String code;

    /**
     * 状态(0:正常;1:禁用)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}