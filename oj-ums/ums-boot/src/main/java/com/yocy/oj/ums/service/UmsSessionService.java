package com.yocy.oj.ums.service;

import com.yocy.oj.ums.model.entity.UmsSession;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 25055
* @description 针对表【ums_session(会话记录表)】的数据库操作Service
* @createDate 2024-04-28 10:08:16
*/
public interface UmsSessionService extends IService<UmsSession> {

    /**
     * 检查远程登录，是的话返回true，否则返回false
     * @param userId
     */
    boolean checkRemoteLogin(Long userId);
}
