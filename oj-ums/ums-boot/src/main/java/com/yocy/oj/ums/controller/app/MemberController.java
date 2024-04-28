package com.yocy.oj.ums.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yocy.common.result.Result;
import com.yocy.oj.ums.dto.MemberAddressDTO;
import com.yocy.oj.ums.dto.MemberAuthDTO;
import com.yocy.oj.ums.dto.MemberRegisterDTO;
import com.yocy.oj.ums.model.entity.UmsMember;
import com.yocy.oj.ums.model.vo.MemberVO;
import com.yocy.oj.ums.service.UmsMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员APP控制器
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Tag(name = "APP-会员模块")
@RestController
@RequiredArgsConstructor
@RequestMapping("/app-api/v1/members")
public class MemberController {

    private final UmsMemberService memberService;

    @Operation(summary = "根据会员ID获取openid")
    @GetMapping("/{memberId}/openid")
    public Result<String> getOpenidById(@Parameter(name = "会员ID") @PathVariable Long memberId) {
        UmsMember member = memberService.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getId, memberId)
                .select(UmsMember::getOpenid));
        String openid = member.getOpenid();
        return Result.success(openid);
    }

    @Operation(summary = "新增会员")
    @PostMapping
    public Result<Long> addMember(@RequestBody MemberRegisterDTO member) {
        Long memberId = memberService.addMember(member);
        return Result.success(memberId);
    }

    @Operation(summary = "获取会员登录信息")
    @GetMapping("/me")
    public Result<MemberVO> getCurrentMemberInfo() {
        MemberVO memberVO = memberService.getCurrentMemberInfo();
        return Result.success(memberVO);
    }

    @Operation(summary = "扣减会员余额")
    @PutMapping("/{memberId}/balances/_deduct")
    public <T> Result<T> deductBalance(@PathVariable Long memberId, @RequestParam Long amount) {
        boolean result = memberService.update(new LambdaUpdateWrapper<UmsMember>()
                .setSql("balance = balance - " + amount)
                .eq(UmsMember::getId, memberId));
        return Result.judge(result);
    }

    @Operation(summary = "根据 openid 获取会员认证信息")
    @GetMapping("/openid/{openid}")
    public Result<MemberAuthDTO> getMemberByOpenid(@Parameter(name = "微信唯一身份标识") @PathVariable String openid) {
        MemberAuthDTO memberAuthInfo = memberService.getMemberByOpenid(openid);
        return Result.success(memberAuthInfo);
    }

    @Operation(summary = "根据手机号码获取会员认证信息", hidden = true)
    @GetMapping("/mobile/{mobile}")
    public Result<MemberAuthDTO> getMemberByMobile(@Parameter(name = "手机号码") @PathVariable String mobile) {
        MemberAuthDTO memberAuthInfo = memberService.getMemberByMobile(mobile);
        return Result.success(memberAuthInfo);
    }

    @Operation(summary = "获取会员地址列表")
    @GetMapping("/{memberId}/addresses")
    public Result<List<MemberAddressDTO>> listMemberAddress(@Parameter(name = "会员ID") @PathVariable Long memberId) {
        List<MemberAddressDTO> addresses = memberService.listMemberAddress(memberId);
        return Result.success(addresses);
    }
}
