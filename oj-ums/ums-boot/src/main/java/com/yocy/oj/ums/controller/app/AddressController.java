package com.yocy.oj.ums.controller.app;

import com.yocy.common.result.Result;
import com.yocy.oj.ums.dto.MemberAddressDTO;
import com.yocy.oj.ums.model.entity.UmsAddress;
import com.yocy.oj.ums.model.form.AddressForm;
import com.yocy.oj.ums.service.UmsAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Tag(name = "APP-会员地址")
@RestController
@RequiredArgsConstructor
@RequestMapping("/app-api/v1/addressed")
public class AddressController {

    private final UmsAddressService addressService;

    @Operation(summary = "获取当前会员的地址列表")
    @GetMapping
    public Result<List<MemberAddressDTO>> listCurrentMemberAddresses() {
        List<MemberAddressDTO> addressList = addressService.listCurrentMemberAddress();
        return Result.success(addressList);
    }

    @Operation(summary = "获取地址详情")
    @GetMapping("/{addressId}")
    public Result<UmsAddress> getAddressDetail(@Parameter(name = "地址ID") @PathVariable Long addressId) {
        UmsAddress address = addressService.getById(addressId);
        return Result.success(address);
    }

    @Operation(summary = "新增地址")
    @PostMapping
    public <T> Result<T> addAddress(@RequestBody @Validated AddressForm addressForm) {
        boolean result = addressService.addAddress(addressForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改地址")
    @PostMapping("/{addressId}")
    public <T> Result<T> updateAddress(
            @Parameter(name = "地址ID") @PathVariable Long addressId,
            @RequestBody @Validated AddressForm addressForm) {
        boolean result = addressService.updateAddress(addressForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除地址")
    @DeleteMapping("/{ids}")
    public <T> Result<T> deleteAddress(@Parameter(name = "地址ID，多个以英文逗号(,)分隔") @PathVariable String ids) {
        boolean result = addressService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.judge(result);
    }


}
