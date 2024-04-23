package com.yocy.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * OAuth2 Endpoint工具类
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
public class OAuth2EndpointUtils {

    public static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    public OAuth2EndpointUtils() {
    }

    /**
     * 从HttpServletRequest中提取参数，并以MultiValueMap的形式返回。
     * 这个方法允许获取到所有重复的参数值，而不仅仅是第一个或最后一个值。
     *
     * @param request HttpServletRequest对象，用于获取请求参数。
     * @return MultiValueMap<String, String> 包含所有请求参数的MultiValueMap，键为参数名，值为参数值的集合。
     */
    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    /**
     * 判断当前请求是否符合授权码grant类型的授权请求。
     *
     * @param request HttpServletRequest对象，代表客户端的HTTP请求。
     * @return boolean 返回true表示请求符合授权码grant类型的授权请求，否则返回false。
     */
    public static boolean matchesAuthorizationCodeGrantRequest(HttpServletRequest request) {
        return AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE)) &&
                request.getParameter(OAuth2ParameterNames.CODE) != null;
    }

    /**
     * 抛出OAuth2认证异常。
     * 该方法用于根据给定的错误代码、参数名称和错误URI创建一个OAuth2Error对象，并基于这个错误对象抛出一个OAuth2AuthenticationException异常。
     *
     * @param errorCode 错误代码，用于标识具体的认证错误。
     * @param parameterName 出错的参数名称，表明哪个OAuth2参数出现了问题。
     * @param errorUri 错误URI，提供了一个可以获取更多错误信息的链接。
     * @throws OAuth2AuthenticationException 根据给定的错误信息抛出的OAuth2认证异常。
     */
    public static void throwError(String errorCode, String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth2.0 Parameter: " + parameterName, errorUri);
        throw new OAuth2AuthenticationException(error);
    }
}
