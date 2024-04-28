package com.yocy.admin.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门查询对象
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Schema(description = "部分分页查询对象")
@Data
public class DeptQuery {

    @Schema(description = "关键字（部门名称）")
    private String keywords;

    @Schema(description = "部门状态")
    private Integer status;
}
