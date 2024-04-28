package com.yocy.oj.ums.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yocy.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 会员表
 * @TableName ums_member
 */
@TableName(value ="ums_member")
@Data
public class UmsMember extends BaseEntity {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * OpenID
     */
    private String openid;

    /**
     * Session密钥
     */
    private String sessionKey;

    /**
     * 状态(1:正常;0:禁用)
     */
    private Integer status;

    /**
     * 积分
     */
    private Integer point;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 简介
     */
    private String description;

    /**
     * github地址
     */
    private String github;

    /**
     * codeforces的username
     */
    private String cfUsername;

    /**
     * 称号
     */
    private String titleName;

    /**
     * 称号颜色
     */
    private String titleColor;

    /**
     * 技能标签
     */
    private String skillTag;

    /**
     * 余额
     */
    private Long balance;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 语言
     */
    private String language;

    /**
     * 是否删除
     */
    @TableLogic(delval = "1", value = "0")
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}