package com.yocy.oj.ums.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yocy.common.base.BaseEntity;
import lombok.Data;

/**
 * 会话记录表
 * @TableName ums_session
 */
@TableName(value ="ums_session")
@Data
public class UmsSession extends BaseEntity {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 访问的浏览器
     */
    private String userAgent;

    /**
     * 访问所在ip
     */
    private String ip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}