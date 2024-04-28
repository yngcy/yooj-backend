package com.yocy.auth.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

/**
 * OAuth2 认证工具类
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
public class OAuth2AuthenticationProviderUtils {

    public OAuth2AuthenticationProviderUtils() {
    }

    /**
     * 尝试获取已认证的客户端信息，如果客户端未认证则抛出 {@link OAuth2AuthenticationException} 异常。
     *
     * @param authentication 当前的认证信息，预期为 {@link OAuth2ClientAuthenticationToken} 实例。
     * @return 如果客户端已认证，则返回对应的 {@link OAuth2ClientAuthenticationToken} 实例。
     * @throws OAuth2AuthenticationException 如果客户端未认证，则抛出此异常，异常中包含 {@link OAuth2ErrorCodes#INVALID_CLIENT} 错误代码。
     */
    public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    /**
     * 使某个OAuth2授权失效。
     * 该方法会将提供的授权对象和令牌标记为失效状态，如果提供的令牌是刷新令牌，则同时将对应的访问令牌和授权码令牌也标记为失效。
     *
     * @param authorization OAuth2授权对象，不可为null。
     * @param token 要标记为失效的OAuth2令牌，其类型必须是OAuth2Token的子类型，不可为null。
     * @return 建立了失效标记的新OAuth2授权对象。
     * @param <T> OAuth2令牌的类型，该类型必须扩展自OAuth2Token。
     */
    public static <T extends OAuth2Token> OAuth2Authorization invalidate(
            OAuth2Authorization authorization, T token) {

        // @formatter:off
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization)
                .token(token,
                        (metadata) ->
                                metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));

        if (OAuth2RefreshToken.class.isAssignableFrom(token.getClass())) {
            authorizationBuilder.token(
                    authorization.getAccessToken().getToken(),
                    (metadata) ->
                            metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));

            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                    authorization.getToken(OAuth2AuthorizationCode.class);
            if (authorizationCode != null && !authorizationCode.isInvalidated()) {
                authorizationBuilder.token(
                        authorizationCode.getToken(),
                        (metadata) ->
                                metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));
            }
        }
        // @formatter:on

        return authorizationBuilder.build();
    }
}
