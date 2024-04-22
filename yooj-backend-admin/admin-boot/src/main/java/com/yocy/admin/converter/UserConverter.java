package com.yocy.admin.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.admin.model.bo.UserBO;
import com.yocy.admin.model.bo.UserFormBO;
import com.yocy.admin.model.bo.UserProfileBO;
import com.yocy.admin.model.entity.SysUser;
import com.yocy.admin.model.form.UserForm;
import com.yocy.admin.model.vo.UserImportVO;
import com.yocy.admin.model.vo.UserInfoVO;
import com.yocy.admin.model.vo.UserPageVO;
import com.yocy.admin.model.vo.UserProfileVO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 用户对象转换器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mappings({
            @Mapping(target = "genderLabel", expression = "java(com.yocy.common.base.IBaseEnum.getLabelByValue(userBO.getGender(),com.yocy.common.enums.GenderEnum.class))")
    })
    UserPageVO bo2VO(UserBO userBO);

    Page<UserPageVO> bo2VO(Page<UserBO> userBOPage);


    UserForm bo2Form(UserFormBO userBO);

    UserForm entity2Form(UserBO userBO);

    @InheritInverseConfiguration(name = "entity2Form")
    SysUser form2Entity(UserForm userForm);

    @Mappings({
            @Mapping(target = "userId", source = "id")
    })
    UserInfoVO entity2UserInfoVO(SysUser entity);

    SysUser importVO2Entity(UserImportVO importVO);

    @Mappings({
            @Mapping(target = "genderLabel", expression = "java(com.yocy.common.base.IBaseEnum.getLabelByValue(profileBO.getGender(),com.yocy.common.enums.GenderEnum.class))")
    })
    UserProfileVO userProfileBO2VO(UserProfileBO profileBO);

}
