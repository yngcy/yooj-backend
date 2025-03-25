package com.yocy.common.web.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Enumeration;

/**
 * Feign相关配置
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Configuration
public class FeignConfig {

    /**
     * 让 DisPatcherServlet 向子线程传递 RequestContext
     * @param servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherServletServletRegistration(DispatcherServlet servlet) {
        servlet.setThreadContextInheritable(true);
        return new ServletRegistrationBean<>(servlet, "/**");
    }

    /**
     * 覆写拦截器，在 feign 发送请求前取出原来的 header 并转发
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
                HttpServletRequest request = attributes.getRequest();
                // 获取请求头
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        // 忽略 content-length，因为在复制请求头到新请求时，原始的 content-length 可能不再准确
                        if (!"content-length".equalsIgnoreCase(name)) {
                            String values = request.getHeader(name);
                            // 将请求头保存到模板中，除了 Content-Length
                            template.header(name, values);
                        }
                    }
                }
            }
        };

    }
}
