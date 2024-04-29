package com.yocy.oj.ums.controller.app;

import com.yocy.common.result.Result;
import com.yocy.oj.ums.service.UmsSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Tag(name = "APP-会员会话")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

    private final UmsSessionService sessionService;

    @Operation(summary = "检查是否异地登录")
    @GetMapping("/{userId}")
    public <T> Result<T> checkRemoteLogin(Long userId) {
        boolean result = sessionService.checkRemoteLogin(userId);
        return Result.judge(result);
    }
}
