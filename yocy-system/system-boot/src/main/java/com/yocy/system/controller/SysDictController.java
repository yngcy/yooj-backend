package com.yocy.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.common.result.PageResult;
import com.yocy.common.result.Result;
import com.yocy.common.web.annotation.PreventDuplicateResubmit;
import com.yocy.common.web.model.Option;
import com.yocy.system.model.form.DictForm;
import com.yocy.system.model.form.DictTypeForm;
import com.yocy.system.model.query.DictPageQuery;
import com.yocy.system.model.query.DictTypePageQuery;
import com.yocy.system.model.vo.DictPageVO;
import com.yocy.system.model.vo.DictTypePageVO;
import com.yocy.system.service.SysDictService;
import com.yocy.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典控制器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Tag(name = "字典接口")
@RestController
@RequestMapping("/api/v1/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictService dictService;

    private final SysDictTypeService dictTypeService;

    @Operation(summary = "字典分页列表")
    @GetMapping("/page")
    public PageResult<DictPageVO> getDictPage(@ParameterObject DictPageQuery queryParams) {
        Page<DictPageVO> result = dictService.getDictPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "字典数据表单")
    @GetMapping("/{id}/form")
    public Result<DictForm> getDictForm(@Parameter(description = "字典ID") @PathVariable Long id) {
        DictForm dictForm = dictService.getDictForm(id);
        return Result.success(dictForm);
    }

    @Operation(summary = "新增字典")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:dict:add')")
    @PreventDuplicateResubmit
    public Result<Void> saveDict(@RequestBody DictForm dictForm) {
        boolean reuslt = dictService.saveDict(dictForm);
        return Result.judge(reuslt);
    }

    @Operation(summary = "修改字典")
    @PutMapping("/{id}")
    @PreAuthorize("@ss.hasPerm('sys:dict:edit')")
    public Result<Void> updateDict(@PathVariable Long id, @RequestBody DictForm dictForm) {
        boolean result = dictService.updateDict(id, dictForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除字典")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:dict:delete')")
    public Result<Void> deleteDict(@Parameter(description = "字典ID，多个以英文逗号(,)分隔") @PathVariable String ids) {
        boolean result = dictService.deleteDict(ids);
        return Result.judge(result);
    }

    @Operation(summary = "字典下拉列表")
    @GetMapping("/options")
    public Result<List<Option>> listDictOptions(@Parameter(description = "字典类型编码") @RequestParam String typeCode) {
        List<Option> options = dictService.listDictOptions(typeCode);
        return Result.success(options);
    }

    @Operation(summary = "字典类型分页列表")
    @GetMapping("/types/page")
    public PageResult<DictTypePageVO> getDictTypePage(
            @ParameterObject DictTypePageQuery queryParams
    ) {
        Page<DictTypePageVO> result = dictTypeService.getDictTypePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增字典数据类型")
    @PostMapping("/types")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:add')")
    @PreventDuplicateResubmit
    public Result<Void> saveDictType(@RequestBody DictTypeForm dictTypeForm) {
        boolean result = dictTypeService.saveDictType(dictTypeForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改字典数据类型")
    @PutMapping("/types")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:edit')")
    public Result<Void> updateDictType(@PathVariable Long id, @RequestBody DictTypeForm dictTypeForm) {
        boolean result = dictTypeService.updateDictType(id, dictTypeForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/types/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:delete')")
    public Result<Void> deleteDictTypes(@Parameter(description = "字典类型ID，多个以英文逗号(,)分隔") @PathVariable String ids) {
        boolean result = dictTypeService.deleteDictTypes(ids);
        return Result.judge(result);
    }

    @Operation(summary = "获取字典类型的数据项")
    @GetMapping("/types/{typeCode}/items")
    public Result<List<Option>> listDictTypeItems(
            @Parameter(description ="字典类型编码") @PathVariable String typeCode
    ) {
        List<Option> list = dictTypeService.listDictItemsByTypeCode(typeCode);
        return Result.success(list);
    }
}
