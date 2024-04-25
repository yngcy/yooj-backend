package com.yocy.admin.converter;


import com.yocy.admin.model.entity.SysMenu;
import com.yocy.admin.model.form.MenuForm;
import com.yocy.admin.model.vo.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MenuConverter {

    @Mappings({
            @Mapping(target = "type", expression = "java(com.yocy.common.base.IBaseEnum.getEnumByValue(entity.getType(), com.yocy.admin.enums.MenuTypeEnum.class))")
    })
    MenuVO entity2VO(SysMenu entity);

    @Mappings({
            @Mapping(target = "type", expression = "java(com.yocy.common.base.IBaseEnum.getEnumByValue(entity.getType(), com.yocy.admin.enums.MenuTypeEnum.class))")
    })
    MenuForm entity2Form(SysMenu entity);

    @Mappings({
            @Mapping(target = "type", expression = "java((Integer)com.yocy.common.base.IBaseEnum.getValueByLabel(menuForm.getType().getLabel(), com.yocy.admin.enums.MenuTypeEnum.class))")
    })
    SysMenu form2Entity(MenuForm menuForm);

}