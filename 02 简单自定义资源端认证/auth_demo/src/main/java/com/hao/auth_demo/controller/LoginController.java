package com.hao.auth_demo.controller;

import cn.hutool.http.HttpUtil;
import com.hao.auth_demo.constant.ApiConstant;
import com.hao.auth_demo.constant.Constant;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    /**
     * 授权码code换取资源token
     * @param code
     * @return
     */
    @GetMapping("/token")
    public String token(String code) {
        String url = ApiConstant.API_TOKEN; //指定URL
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("grant_type", "authorization_code");
        map.put("code", code);
        map.put("redirect_uri", "http://127.0.0.1:8080/login");
        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("Authorization", Constant.BASIC_AUTH);
        String result= HttpUtil.createPost(url).addHeaders(headers).form(map).execute().body();
        return result;
    }

    /**
     * 注销token
     * @param code
     * @return
     */
    @GetMapping("/logout")
    public String logout(String code) {
        String url = Constant.AUTH_SERVER + "/api/v1/logout";//指定URL
        String result= HttpUtil.createGet(url).execute().body();
        return result;
    }

    /**
     * 微信登录code换取token
     * @param requestMap
     * @return
     */
    @PostMapping("/wechatLogin")
    public String wechatLogin(@RequestBody Map<String,String> requestMap) {
        String code = requestMap.get("code");
        // 地址
        String url = ApiConstant.API_WECHAT_LOGIN;
        // 请求头
        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.put("X-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 /15A372 Safari/604.1 wechatdevtools/1.06.2307260 MicroMessenger/8.0.5 Language/zh_CN webview/");
        headers.put("X-client-id", Constant.CLIENT_ID);
        headers.put("X-operating-sys-version","windows10.1.1");
        headers.put("X-device-fingerprint","11111");
        System.out.println(headers);
        // 参数
        JSONObject jsonObject = new JSONObject();//存放参数
        jsonObject.put("code", code);


        String result= HttpUtil.createPost(url).addHeaders(headers).body(jsonObject.toJSONString()).execute().body();
        return result;
    }
}
