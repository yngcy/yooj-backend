package com.yocy.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yocy.common.base.IBaseEnum;
import lombok.Getter;

/**
 * 菜单类型枚举
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
public enum MenuTypeEnum implements IBaseEnum<Integer> {
    NULL(0, null),
    MENU(1, "菜单"),
    CATALOG(2, "目录"),
    EXTLINK(3, "外链"),
    BUTTON(4, "按钮");



    @Getter
    @EnumValue
    private Integer value;

    @Getter
    private String label;

    MenuTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
