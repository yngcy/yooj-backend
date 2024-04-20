package com.yocy.admin.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户分页查询对象
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Schema
@Data
public class UserPageQuery {

    @Schema(description="关键字(用户名/昵称/手机号)")
    private String keywords;

    @Schema(description="用户状态")
    private Integer status;

    @Schema(description="部门ID")
    private Long deptId;
}
