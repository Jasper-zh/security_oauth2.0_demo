package com.hao.auth_demo.config;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hao.auth_demo.constant.ApiConstant;
import com.hao.auth_demo.constant.Constant;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;


/**
 * 自定义 OAuth2 令牌服务
 * 支持 JWT 令牌和非-JWT 令牌
 */
@Service
public class CustomResourceServerTokenServices implements ResourceServerTokenServices {

    /**
     * 根据令牌值加载身份验证信息
     * @param accessTokenValue
     * @return
     * @throws AuthenticationException
     * @throws InvalidTokenException
     */
    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        if (accessTokenValue == null || accessTokenValue.isEmpty()) {
            throw new InvalidTokenException("Invalid token");
        }
        // 根据令牌类型执行不同的验证和解析逻辑
        if (isJWTToken(accessTokenValue)) {
            // 如果是 JWT 令牌，调用 JWT 验证和解析逻辑
            return validateAndParseJWT(accessTokenValue);
        } else {
            // 如果是非-JWT 令牌，调用 RemoteTokenServices 验证逻辑
            return validateNonJWTToken(accessTokenValue);
        }
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    /**
     * 检查令牌是否为 JWT 令牌
     * 供 loadAuthentication() 方法调用
     * @param token
     * @return
     */
    private boolean isJWTToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        // 检查令牌是否包含点号
        if (token.split("\\.").length < 3) {
            return false;
        }
        // 尝试解码头部和载荷
        try {
            String[] parts = token.split("\\.");
            String header = new String(Base64.getDecoder().decode(parts[0]));
            String payload = new String(Base64.getDecoder().decode(parts[1]));
            // 检查头部是否包含 alg 字段
            if (header.contains("\"alg\":")) {
                return true;
            }
        } catch (Exception e) {
            // 解码失败，不是 JWT 令牌
        }
        return false;
    }


    /**
     * 验证并解析 JWT 令牌
     * 供 loadAuthentication() 方法调用
     * @param jwtToken
     * @return
     */
    private OAuth2Authentication validateAndParseJWT(String jwtToken) {
        // 实现 JWT 验证和解析逻辑
        if (!verifySignature(jwtToken)) {
            throw new InvalidTokenException("Invalid token");
        }
        try {
            // 在这里解析JWT并从中提取有关身份验证的信息
            JWT jwt = JWTParser.parse(jwtToken);
            // 从JWT中提取必要的信息，例如身份信息、权限等
            String subject = jwt.getJWTClaimsSet().getSubject();
            // 取中间部分
            String payload = jwtToken.split("\\.")[1];
            // 解码
            String json = new String(Base64.getDecoder().decode(payload));
            // 转换成JSON对象
            JSONObject jsonObject = JSON.parseObject(json);
            // 获取用户信息
            String name = jsonObject.getString("name");
            String userName = jsonObject.getString("userName");
            String phone = jsonObject.getString("mobile");
            String email = jsonObject.getString("email");
            // 创建用户对象
            UserDetails userDetails = createUser(name, userName, phone, email);

            // 创建一个包含身份信息的OAuth2Authentication对象
            // 这只是一个示例，您需要根据实际需求构建OAuth2Authentication对象
            // 在这里，您可以使用一些用户信息和权限来构建OAuth2Authentication对象
            OAuth2Request oauth2Request = new OAuth2Request(null, Constant.CLIENT_ID, null, true, null, null, null, null, null);
            List<GrantedAuthority> authorities = new ArrayList<>(); // 添加用户权限
            Authentication user = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            OAuth2Authentication authentication = new OAuth2Authentication(oauth2Request, user);
            return authentication;
        } catch (Exception e) {
            // 在解析或处理JWT时出现错误
            throw new InvalidTokenException("Invalid token");
        }
    }

    /**
     * 创建用户对象
     * 供 validateAndParseJWT() 方法调用
     * @param name
     * @param userName
     * @param phone
     * @param email
     * @return
     */
    private static UserDetails createUser(String name, String userName, String phone, String email) {
        // 在此处根据提取的信息创建用户对象，这只是一个示例
        // 您可能需要实现一个自定义的 UserDetailsService 来从数据库或其他地方检索用户信息
        // 并返回实现了 UserDetails 接口的用户对象

        // 创建一个简单的 User 对象作为示例
        User user = new User(userName, "", Collections.emptyList());

        // 在这里可以设置用户的其他属性，例如姓名、电话和电子邮件

        return user;
    }

    /**
     * 验证非 JWT 令牌
     * 供 loadAuthentication() 方法调用
     * @param nonJWTToken
     * @return
     */
    private OAuth2Authentication validateNonJWTToken(String nonJWTToken) {
        // 调用 RemoteTokenServices 进行令牌验证
        // 使用 RemoteTokenServices 进行远程令牌验证
        RemoteTokenServices remoteTokenServices = tokenServices();
        OAuth2Authentication authentication = remoteTokenServices.loadAuthentication(nonJWTToken);

        // 根据验证结果创建 OAuth2Authentication
        if (authentication != null) {
            // 根据需要，您可能还需要对 authentication 进行一些处理
            return authentication;
        } else {
            throw new InvalidTokenException("Invalid token");
        }
    }

    public RemoteTokenServices tokenServices(){
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl(ApiConstant.API_CHECK_TOKEN);
        remoteTokenServices.setClientId(Constant.CLIENT_ID);
        remoteTokenServices.setClientSecret(Constant.CLIENT_SECRET);
        return remoteTokenServices;
    }

    private List<RSAKey> getPublicKeys() throws ParseException {
        // 读取认证中心JWT公钥列表
        List<RSAKey> rsaKeys = new ArrayList<>();
        HttpResponse execute = HttpUtil.createGet("https://dfcvtest.bccastle.com/api/v1/oauth2/keys").execute();
        if (execute.getStatus() == 200){
            String body = execute.body();
            JSONObject bodyJSON = JSON.parseObject(body);
            JSONArray keysJSON = JSONObject.parseArray(bodyJSON.getString("keys"));
            for (Object key : keysJSON) {
                String jsonObject = JSONObject.parseObject(key.toString()).toJSONString();
                rsaKeys.add(RSAKey.parse(jsonObject));
            }
            return rsaKeys;
        }else {
            System.out.println("调用 /api/v1/oauth2/keys 获取公钥列表失败！");
            return new ArrayList<>();
        }
    }

    private boolean verifySignature(String id_token) {
        try {
            JWT jwtToken = JWTParser.parse(id_token);
            SignedJWT jwt = (SignedJWT)jwtToken;
            List<RSAKey> publicKeyList = getPublicKeys();
            RSAKey rsaKey = null;
            for (RSAKey key : publicKeyList) {
                if (jwt.getHeader().getKeyID().equals(key.getKeyID())) {
                    rsaKey = key;
                }
            }
            if (rsaKey != null) {
                RSASSAVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
                return jwt.verify(verifier);
            }else {
                System.out.println("无法验证令牌签名");
                return false;
            }
        } catch (Exception e) {
            System.out.println("验证令牌签名失败！"+e.getMessage());
            return false;
        }
    }
}