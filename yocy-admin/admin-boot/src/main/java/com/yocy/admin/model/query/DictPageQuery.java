package com.yocy.admin.model.query;

import com.yocy.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典数据项分页查询对象
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Schema(description = "字典数据项分页查询对象")
@Data
public class DictPageQuery extends BasePageQuery {

    /**
     * 关键字
     */
    @Schema(description = "关键字(字典项名称)")
    private String keywords;

    /**
     * 字典类型编码
     */
    @Schema(description = "字典类型编码")
    private String typeCode;
}
