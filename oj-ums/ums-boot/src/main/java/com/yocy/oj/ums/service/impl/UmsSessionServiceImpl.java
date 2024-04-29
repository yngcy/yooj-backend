package com.yocy.oj.ums.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.oj.ums.mapper.UmsSessionMapper;
import com.yocy.oj.ums.model.entity.UmsSession;
import com.yocy.oj.ums.service.UmsSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author 25055
* @description 针对表【ums_session(会话记录表)】的数据库操作Service实现
* @createDate 2024-04-28 10:08:16
*/
@Service
public class UmsSessionServiceImpl extends ServiceImpl<UmsSessionMapper, UmsSession>
    implements UmsSessionService{


    /**
     * 检验是否异地登录，如果异地登录，则通知用户
     * @param userId
     */
    @Override
    public boolean checkRemoteLogin(Long userId) {
        List<UmsSession> sessionList = this.baseMapper.selectList(new LambdaQueryWrapper<UmsSession>()
                .eq(UmsSession::getMemberId, userId)
                .orderByDesc(UmsSession::getCreateTime)
                .last("limit 2"));
        if (sessionList.size() < 2) {
            return false;
        }

        UmsSession currentSession = sessionList.get(0);
        UmsSession lastSession = sessionList.get(1);
        // TODO 若两次登录ip不同，需要通知用户
        if (!currentSession.getIp().equals(lastSession.getIp())) {
            String remoteLoginContent = getRemoteLoginContent(lastSession.getIp(), currentSession.getIp(), currentSession.getCreateTime());
            if (remoteLoginContent == null) {
                return false;
            }
            // TODO 通知用户
            System.out.println(remoteLoginContent);
        }

        return true;
    }

    private String getRemoteLoginContent(String oldIp, String newIp, LocalDateTime loginDate) {
        String dateStr = DateUtil.format(loginDate, "yyyy-mm-dd HH:mm:ss");
        StringBuilder result = new StringBuilder();
        result.append("亲爱的会员，您好！您的账号于").append(dateStr);
        String addr = null;
        try {
            String newRes = HttpUtil.get("https://whois.pconline.com.cn/ipJson.jsp?ip=" + newIp + "&json=true");
            JSONObject newResJson = JSONUtil.parseObj(newRes);
            addr = newResJson.getStr("addr");
            String newCityCode = newResJson.getStr("cityCode");

            String oldRes = HttpUtil.get("https://whois.pconlie.com.cn/ipJson.jsp?ip=" + oldIp + "&json=true");
            JSONObject oldResJson = JSONUtil.parseObj(oldRes);
            String oldCityCode = oldResJson.getStr("cityCode");
            if (newCityCode == null || oldCityCode == null || newCityCode.equals(oldCityCode)) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (StrUtil.isNotBlank(addr)) {
            result.append("在【")
                    .append(addr)
                    .append("】");
        }
        result.append("登录了，登录IP为：【")
                .append(newIp)
                .append("】请您核实账号安全。若非本人操作，请立即修改密码。")
                .append("\n\n")
                .append("Hello, Dear member! Your account was logged in in");
        if (StrUtil.isNotBlank(addr)) {
            result.append("【")
                    .append(addr)
                    .append("】on ")
                    .append(". Please verify the security of your account. If you do not operate by yourself, please change your password immediately.");
        }

        return result.toString();
    }

}




