package com.yocy.auth.model;

import lombok.Builder;
import lombok.Data;

/**
 * 验证码响应对象
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Data
@Builder
public class CaptchaResult {

    /**
     * 验证码唯一标识（用于从Redis中获取验证码Code）
     */
    private String captchaId;

    /**
     * 验证码图片Base64字符串
     */
    private String captchaBase64;
}
