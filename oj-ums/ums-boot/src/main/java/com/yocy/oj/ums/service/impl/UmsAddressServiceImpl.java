package com.yocy.oj.ums.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.common.constant.GlobalConstants;
import com.yocy.common.security.utils.SecurityUtils;
import com.yocy.oj.ums.dto.MemberAddressDTO;
import com.yocy.oj.ums.mapper.UmsAddressMapper;
import com.yocy.oj.ums.model.entity.UmsAddress;
import com.yocy.oj.ums.model.form.AddressForm;
import com.yocy.oj.ums.service.UmsAddressService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author 25055
* @description 针对表【ums_address(会员地址表)】的数据库操作Service实现
* @createDate 2024-04-28 10:09:51
*/
@Service
public class UmsAddressServiceImpl extends ServiceImpl<UmsAddressMapper, UmsAddress>
    implements UmsAddressService{

    @Override
    public boolean addAddress(AddressForm addressForm) {
        Long memberId = SecurityUtils.getMemberId();

        UmsAddress address = new UmsAddress();
        BeanUtil.copyProperties(addressForm, address);
        address.setMemberId(memberId);
        boolean result = this.save(address);
        if (result) {
            // 修改其他地址为非默认
            if (GlobalConstants.STATUS_YES.equals(addressForm.getDefaulted())) {
                this.update(new LambdaUpdateWrapper<UmsAddress>()
                        .eq(UmsAddress::getMemberId, memberId)
                        .eq(UmsAddress::getDefaulted, 1)
                        .ne(UmsAddress::getId, address.getId())
                        .set(UmsAddress::getDefaulted, 0));
            }
        }
        return result;
    }

    @Override
    public boolean updateAddress(AddressForm addressForm) {
        Long memberId = SecurityUtils.getMemberId();

        UmsAddress address = new UmsAddress();
        BeanUtil.copyProperties(addressForm, address);
        boolean result = this.updateById(address);
        if (result) {
            if (GlobalConstants.STATUS_YES.equals(addressForm.getDefaulted())) {
                this.update(new LambdaUpdateWrapper<UmsAddress>()
                        .eq(UmsAddress::getMemberId, memberId)
                        .eq(UmsAddress::getDefaulted, 1)
                        .ne(UmsAddress::getId, address.getId())
                        .set(UmsAddress::getDefaulted, 0));
            }
        }
        return result;
    }

    @Override
    public List<MemberAddressDTO> listCurrentMemberAddress() {
        Long memberId = SecurityUtils.getMemberId();
        List<UmsAddress> umsAddressList = this.list(new LambdaQueryWrapper<UmsAddress>()
                .eq(UmsAddress::getMemberId, memberId)
                .orderByDesc(UmsAddress::getDefaulted) // 默认地址排在首位
        );
        List<MemberAddressDTO> memberAddressList = Optional.ofNullable(umsAddressList).orElse(new ArrayList<>()).stream()
                .map(umsAddress -> {
                    MemberAddressDTO memberAddressDTO = new MemberAddressDTO();
                    BeanUtil.copyProperties(umsAddress, memberAddressDTO);
                    return memberAddressDTO;
                }).collect(Collectors.toList());
        return memberAddressList;
    }
}




