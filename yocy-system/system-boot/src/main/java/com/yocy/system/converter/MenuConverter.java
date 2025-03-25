package com.yocy.system.converter;


import com.yocy.system.model.entity.SysMenu;
import com.yocy.system.model.form.MenuForm;
import com.yocy.system.model.vo.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MenuConverter {

    @Mappings({
            @Mapping(target = "type", expression = "java(com.yocy.common.base.IBaseEnum.getEnumByValue(entity.getType(), com.yocy.system.enums.MenuTypeEnum.class))")
    })
    MenuVO entity2VO(SysMenu entity);

    @Mappings({
            @Mapping(target = "type", expression = "java(com.yocy.common.base.IBaseEnum.getEnumByValue(entity.getType(), com.yocy.system.enums.MenuTypeEnum.class))")
    })
    MenuForm entity2Form(SysMenu entity);

    @Mappings({
            @Mapping(target = "type", expression = "java((Integer)com.yocy.common.base.IBaseEnum.getValueByLabel(menuForm.getType().getLabel(), com.yocy.system.enums.MenuTypeEnum.class))")
    })
    SysMenu form2Entity(MenuForm menuForm);

}