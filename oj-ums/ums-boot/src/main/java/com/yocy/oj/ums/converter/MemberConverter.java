package com.yocy.oj.ums.converter;

import com.yocy.oj.ums.dto.MemberAuthDTO;
import com.yocy.oj.ums.dto.MemberInfoDTO;
import com.yocy.oj.ums.dto.MemberRegisterDTO;
import com.yocy.oj.ums.model.entity.UmsMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 会员对象转换器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Mapper(componentModel = "spring")
public interface MemberConverter {


    @Mappings({
            @Mapping(target = "username", source = "openid")
    })
    MemberAuthDTO entity2OpenidAuthDTO(UmsMember entity);

    @Mappings({
            @Mapping(target = "username", source = "mobile")
    })
    MemberAuthDTO entity2MobileAuthDTO(UmsMember entity);


    MemberInfoDTO entity2MemberInfoDTO(UmsMember entity);

    UmsMember dto2Entity(MemberRegisterDTO memberRegisterDTO);
}
