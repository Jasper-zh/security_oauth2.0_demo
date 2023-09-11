package com.hao.auth_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private RemoteTokenServices remoteTokenServices;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().
                requestMatchers().antMatchers("/info","/add","/admin");
    }

    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(remoteTokenServices);
    }

    /**
     * 配置远端认证服务地址及客户端凭证（配置到yaml了去掉该代码）
     * @return
     */
//    public ResourceServerTokenServices tokenServices(){
//        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9998/oauth/check_token");
//        remoteTokenServices.setClientId("auth_demo");
//        remoteTokenServices.setClientSecret("123456");
//        return remoteTokenServices;
//    }
}
