package com.yocy.oj.ums.model.form;

import com.yocy.common.web.constraint.CheckCityValid;
import com.yocy.common.web.constraint.CityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 地址表单对象
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Schema(description = "地址表单对象")
@Data
public class AddressForm {

    @Schema(description="地址ID")
    private Long id;

    @Schema(description="收货人姓名")
    private String consigneeName;

    @Schema(description="收货人手机号")
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "{phone.valid}")
    private String consigneePhone;

    @Schema(description="省")
    @CheckCityValid(CityType.PROVINCE)
    private String province;

    @Schema(description="市")
    @CheckCityValid(CityType.CITY)
    private String city;

    @Schema(description="区")
    @CheckCityValid(CityType.AREA)
    private String area;

    @Schema(description="详细地址")
    @Length(min = 1, max = 100, message = "{text.length.min}，{text.length.max}")
    private String detailAddress;

    @Schema(description="是否默认地址(1:是;0:否)")
    private Integer defaulted;


}
