package com.yocy.system.api;

import com.yocy.system.api.fallback.UserFeignFallbackClient;
import com.yocy.system.dto.UserAuthInfo;
import com.yocy.common.web.config.FeignDecoderConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@FeignClient(value = "yocy-system", fallback = UserFeignFallbackClient.class, configuration = {FeignDecoderConfig.class})
public interface UserFeignClient {

    @GetMapping("/api/v1/users/{username}/authInfo")
    UserAuthInfo getUserAuthInfo(@PathVariable String username);
}
