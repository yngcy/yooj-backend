package com.yocy.common.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yocy.common.result.Result;
import com.yocy.common.result.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义无权限异常
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Component
public class MyAccessDeinedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), Result.failed(ResultCode.ACCESS_UNAUTHORIZED));
    }
}
