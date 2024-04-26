package com.yocy.admin.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Schema(description = "字典表单对象")
@Data
public class DictForm {

    @Schema(description="字典ID")
    private Long id;

    @Schema(description="类型编码")
    private String typeCode;

    @Schema(description="字典名称")
    private String name;

    @Schema(description="字典值")
    private String value;

    @Schema(description="状态(1:启用;0:禁用)")
    private Integer status;

    @Schema(description="排序")
    private Integer sort;

    @Schema(description = "字典备注")
    private String remark;
}
