package com.yocy.admin.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.admin.model.entity.SysDictType;
import com.yocy.admin.model.form.DictTypeForm;
import com.yocy.admin.model.vo.DictTypePageVO;
import org.mapstruct.Mapper;

/**
 * 字典数据类型对象转换器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Mapper(componentModel = "spring")
public interface DictTypeConverter {

    Page<DictTypePageVO> entityPage2VOPage(Page<SysDictType> page);

    DictTypeForm entity2Form(SysDictType entity);

    SysDictType form2Entity(DictTypeForm form);
}
