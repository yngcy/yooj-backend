package com.yocy.admin.api;

import com.yocy.admin.api.fallback.UserFeignFallbackClient;
import com.yocy.admin.dto.UserAuthInfo;
import com.yocy.common.web.config.FeignDecoderConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@FeignClient(value = "yocy-admin", fallback = UserFeignFallbackClient.class, configuration = {FeignDecoderConfig.class})
public interface UserFeignClient {

    @GetMapping("/api/v1/users/{username}/authInfo")
    UserAuthInfo getUserAuthInfo(@PathVariable String username);
}
