package com.yocy.system.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.system.model.entity.SysRole;
import com.yocy.system.model.form.RoleForm;
import com.yocy.system.model.vo.RolePageVO;
import com.yocy.common.web.model.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 角色对象转换器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {

    Page<RolePageVO> entityPage2VOPage(Page<SysRole> page);

    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name")
    })
    Option entity2Option(SysRole role);

    List<Option> entities2Options(List<SysRole> roles);

    SysRole form2Entity(RoleForm roleForm);

    RoleForm entity2Form(SysRole entity);
}
