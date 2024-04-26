package com.yocy.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.admin.model.entity.SysDict;
import com.yocy.admin.model.form.DictForm;
import com.yocy.admin.model.query.DictPageQuery;
import com.yocy.admin.model.vo.DictPageVO;
import com.yocy.common.web.model.Option;

import java.util.List;

/**
* @author 25055
* @description 针对表【sys_dict(字典数据表)】的数据库操作Service
* @createDate 2024-04-20 00:27:02
*/
public interface SysDictService extends IService<SysDict> {

    /**
     * 字典数据项分页列表
     *
     * @param queryParams
     * @return
     */
    Page<DictPageVO> getDictPage(DictPageQuery queryParams);

    /**
     * 字典数据项表单
     *
     * @param id 字典数据项ID
     * @return
     */
    DictForm getDictForm(Long id);

    /**
     * 新增字典数据项
     *
     * @param dictForm 字典数据项表单
     * @return
     */
    boolean saveDict(DictForm dictForm);

    /**
     * 修改字典数据项
     *
     * @param id       字典数据项ID
     * @param dictForm 字典数据项表单
     * @return
     */
    boolean updateDict(Long id, DictForm dictForm);

    /**
     * 删除字典数据项
     *
     * @param idsStr 字典数据项ID，多个以英文逗号(,)分割
     * @return
     */
    boolean deleteDict(String idsStr);

    /**
     * 获取字典下拉列表
     *
     * @param typeCode
     * @return
     */
    List<Option> listDictOptions(String typeCode);
}
