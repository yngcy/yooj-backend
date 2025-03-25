package com.yocy.system.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.system.model.entity.SysDict;
import com.yocy.system.model.form.DictForm;
import com.yocy.system.model.vo.DictPageVO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * 字典数据项转换器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Mapper(componentModel = "spring")
public interface DictConverter {
    Page<DictPageVO> entityPage2VOPage(Page<SysDict> page);

    DictForm entity2Form(SysDict entity);

    @InheritInverseConfiguration(name="entity2Form")
    SysDict form2Entity(DictForm form);
}
