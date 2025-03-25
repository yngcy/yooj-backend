package com.yocy.system.model.query;

import com.yocy.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色分页查询
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Schema(description = "角色分页查询对象")
@Data
public class RolePageQuery extends BasePageQuery {

    @Schema(description = "关键字(角色名称/角色编码)")
    private String keywords;
}
