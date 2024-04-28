package com.yocy.admin.model.query;

import com.yocy.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典类型数据分页请求
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Schema(description = "字典类型分页查询对象")
@Data
public class DictTypePageQuery extends BasePageQuery {

    /**
     * 关键字
     */
    @Schema(description = "关键字(类型名称/类型编码)")
    private String keywords;
}
