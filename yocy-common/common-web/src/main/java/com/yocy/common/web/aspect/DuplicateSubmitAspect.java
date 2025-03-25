package com.yocy.common.web.aspect;

import cn.hutool.core.util.StrUtil;
import com.yocy.common.result.ResultCode;
import com.yocy.common.security.utils.SecurityUtils;
import com.yocy.common.web.annotation.PreventDuplicateResubmit;
import com.yocy.common.web.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * 防止重复提交切面
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DuplicateSubmitAspect {

    private final RedissonClient redissonClient;

    private static final String RESUBMIT_LOCK_PREFIX = "LOCK:RESUBMIT:";

    @Pointcut("@annotation(preventDuplicateResubmit)")
    public void preventDuplicateSubmitPointCut(PreventDuplicateResubmit preventDuplicateResubmit) {
        log.info("定义防止重复提交切点");
    }

    @Around("preventDuplicateSubmitPointCut(preventDuplicateResubmit)")
    public Object doAround(ProceedingJoinPoint pjp, PreventDuplicateResubmit preventDuplicateResubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String jti = SecurityUtils.getJti();
        if (StrUtil.isNotBlank(jti)) {
            String resubmitLockKey = RESUBMIT_LOCK_PREFIX + jti + ":" + request.getMethod() + "-" + request.getRequestURI();
            int expire = preventDuplicateResubmit.expire();
            RLock lock = redissonClient.getLock(resubmitLockKey);
            boolean lockResult = lock.tryLock(0, expire, TimeUnit.SECONDS);
            if (!lockResult) {
                throw new BizException(ResultCode.REPEAT_SUBMIT_ERROR);
            }
        }

        return pjp.proceed();
    }
}
