package com.hao.auth_demo.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CustomTokenService implements ResourceServerTokenServices {

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {

        // 根据请求来源选择不同的远程认证服务
        OAuth2Authentication authentication = getTokenService().loadAuthentication(accessToken);

        if (authentication == null) {
            throw new InvalidTokenException("Invalid access token");
        }

        // 这里可以对用户信息进行处理，例如根据用户手机号查询此应用对应用户信息，再构建新的 Authentication 返回
        // CustomUserDetails customUserDetails = remoteUserService.getUserInfoByPhone(phone);
        // Authentication newUserAuthentication = new UsernamePasswordAuthenticationToken(customUserDetails, userAuthentication.getCredentials(), AuthorityUtils.commaSeparatedStringToAuthorityList("user"));
        // OAuth2Authentication newOAuth2Authentication = new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), newUserAuthentication);

        return authentication;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String token) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    // 实现此方法来获取请求来源
    private ResourceServerTokenServices getTokenService() {
        // 根据请求头或其他标识返回请求来源，例如根据请求头中的 User-Agent 判断是 H5 还是 PC
        // 这里只是示例，请根据您的实际需求实现
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userAgent = request.getHeader("X-Agent");
        if (userAgent != null && userAgent.contains("H5")) {
            return tokenServicesH5();
        } else {
            return tokenServicesPC();
        }
    }

    /**
     * 配置远端认证服务地址及客户端凭证（配置到yaml了去掉该代码）
     * @return
     */
    public ResourceServerTokenServices tokenServicesH5(){
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9998/oauth/check_token");
        remoteTokenServices.setClientId("auth_demo");
        remoteTokenServices.setClientSecret("123456");
        return remoteTokenServices;
    }

    public ResourceServerTokenServices tokenServicesPC(){
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9997/oauth/check_token");
        remoteTokenServices.setClientId("auth_demo");
        remoteTokenServices.setClientSecret("123456");
        return remoteTokenServices;
    }


}
