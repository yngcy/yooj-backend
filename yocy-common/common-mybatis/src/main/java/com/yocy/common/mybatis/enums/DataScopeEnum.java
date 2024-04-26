package com.yocy.common.mybatis.enums;

import com.yocy.common.base.IBaseEnum;
import lombok.Getter;

/**
 * 数据权限枚举
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Getter
public enum DataScopeEnum implements IBaseEnum<Integer> {
    ALL(0, "所有数据"),
    DEPT_AND_SUB(1, "本部门及子部门数据"),
    DEPT(2, "本部门数据"),
    SELF(3, "仅本人数据");

    private Integer value;

    private String label;

    DataScopeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
