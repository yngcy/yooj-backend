package com.yocy.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * MP 数据权限注解
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    /**
     * 数据权限 {@link com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor}
     */
    String deptAlias() default "";

    String deptIdColumnName() default "dept_id";
    String userAlias() default "";

    String userIdColumnName() default "create_by";
}
