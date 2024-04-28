package com.yocy.oj.ums.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员视图对象
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Schema(description = "会员视图对象")
@Data
public class MemberVO {

    @Schema(description = "会员ID")
    private Long id;

    @Schema(description = "会员昵称")
    private String nickName;

    @Schema(description = "会员头像地址")
    private String avatarUrl;

    @Schema(description = "会员手机号")
    private String mobile;

    @Schema(description = "会员余额(单位:元)")
    private String balance;

}
