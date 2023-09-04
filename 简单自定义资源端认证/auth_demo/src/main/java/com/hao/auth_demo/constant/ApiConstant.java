package com.hao.auth_demo.constant;

import static com.hao.auth_demo.constant.Constant.AUTH_SERVER;

public class ApiConstant {
    // API_URL
    public static final String API_TOKEN = AUTH_SERVER + "/oauth2/token";
    public static final String API_CHECK_TOKEN = AUTH_SERVER + "/oauth2/check_token";
    public static final String API_LOGOUT = AUTH_SERVER + "/logout";
    // public static final String API_WECHAT_LOGIN = AUTH_SERVER + "/api/v2/sdk/login/wechat-miniprogram";
    public static final String API_KEYS = AUTH_SERVER + "/oauth2/keys";
}
