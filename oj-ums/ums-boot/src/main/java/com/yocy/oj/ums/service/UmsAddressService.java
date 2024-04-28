package com.yocy.oj.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.oj.ums.dto.MemberAddressDTO;
import com.yocy.oj.ums.model.entity.UmsAddress;
import com.yocy.oj.ums.model.form.AddressForm;

import java.util.List;

/**
* @author 25055
* @description 针对表【ums_address(会员地址表)】的数据库操作Service
* @createDate 2024-04-28 10:09:51
*/
public interface UmsAddressService extends IService<UmsAddress> {

    /**
     * 新增地址
     * @param addressForm
     * @return
     */
    boolean addAddress(AddressForm addressForm);

    /**
     * 修改地址
     * @param addressForm
     * @return
     */
    boolean updateAddress(AddressForm addressForm);

    /**
     * 获取当前登录用户的地址列表
     * @return
     */
    List<MemberAddressDTO> listCurrentMemberAddress();
}
