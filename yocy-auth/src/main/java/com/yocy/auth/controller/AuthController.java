package com.yocy.auth.controller;

import com.yocy.auth.model.CaptchaResult;
import com.yocy.auth.service.AuthService;
import com.yocy.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * <br/>
 * 获取验证码、退出登录等接口
 * <br/>
 * 注：登录接口不在此控制器，在过滤器OAuth2TokenEndpointFilter拦截端点（/oath2/token）处理
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public Result<CaptchaResult> getCaptcha() {
        CaptchaResult captchaResult = authService.getCaptcha();
        return Result.success(captchaResult);
    }

    @Operation(summary = "发送手机短信验证码")
    @PostMapping("/sms_code")
    public Result<Void> sendLoginSmsCode(
            @Parameter(description = "手机号") @RequestParam String mobile
    ) {
        boolean result = authService.sendLoginSmsCode(mobile);
        return Result.judge(result);
    }
}
