package com.yocy.admin.converter;


import com.yocy.admin.model.entity.SysMenu;
import com.yocy.admin.model.form.MenuForm;
import com.yocy.admin.model.vo.MenuVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuConverter {

    MenuVO entity2VO(SysMenu entity);


    MenuForm entity2Form(SysMenu entity);

    SysMenu form2Entity(MenuForm menuForm);

}