package com.yocy.oj.ums.converter;

import com.yocy.oj.ums.dto.MemberAddressDTO;
import com.yocy.oj.ums.model.entity.UmsAddress;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 会员地址对象转换器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Mapper(componentModel = "spring")
public interface AddressConverter {

    MemberAddressDTO entity2DTO(UmsAddress entity);

    List<MemberAddressDTO> entity2DTO(List<UmsAddress> entity);
}
