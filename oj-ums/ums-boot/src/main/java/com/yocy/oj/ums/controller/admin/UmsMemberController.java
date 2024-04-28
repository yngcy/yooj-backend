package com.yocy.oj.ums.controller.admin;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.common.constant.GlobalConstants;
import com.yocy.common.result.PageResult;
import com.yocy.common.result.Result;
import com.yocy.oj.ums.model.entity.UmsMember;
import com.yocy.oj.ums.service.UmsMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 会员管理接口
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Tag(name = "admin-会员管理")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class UmsMemberController {

    private final UmsMemberService memberService;

    @Operation(summary = "会员分页列表")
    @GetMapping
    public PageResult<UmsMember> getMemberPage(
            @Parameter(name = "页码") Long pageNum,
            @Parameter(name = "每页数量") Long pageSize,
            @Parameter(name = "会员昵称") String nickName) {
        Page<UmsMember> result = memberService.list(new Page<>(pageNum, pageSize), nickName);

        return PageResult.success(result);
    }


    @Operation(summary = "修改会员")
    @PutMapping(value = "/{memberId}")
    public <T> Result<T> update(
            @Parameter(name = "会员ID") @PathVariable Long memberId,
            @RequestBody UmsMember member) {
        boolean result = memberService.updateById(member);
        return Result.judge(result);
    }

    @Operation(summary = "修改会员状态")
    @PatchMapping("/{memberId}/status")
    public <T> Result<T> updateMemberStatus(
            @Parameter(name = "会员ID") @PathVariable Long memberId,
            @RequestBody UmsMember member) {
        boolean result = memberService.update(new LambdaUpdateWrapper<UmsMember>()
                .eq(UmsMember::getId, memberId)
                .set(UmsMember::getStatus, member.getStatus()));
        return Result.judge(result);
    }

    @Operation(summary = "删除会员")
    @DeleteMapping("/{ids}")
    public <T> Result<T> delete(@Parameter(name = "会员ID，多个以英文逗号(,)分隔") @PathVariable String ids) {
        boolean result = memberService.update(new LambdaUpdateWrapper<UmsMember>()
                .eq(UmsMember::getId, Arrays.asList(ids.split(",")))
                .set(UmsMember::getDeleted, GlobalConstants.STATUS_YES));

        return Result.judge(result);
    }



}
