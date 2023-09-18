package com.hao.auth_demo.constant;

public class Constant {
    /**
     * 服务器地址
     */
    public static final String AUTH_SERVER = "https://www.yourserver.com";
    /**
     * 客户端ID
     */
    public static final String CLIENT_ID = "xxx";
    /**
     * 客户端密钥
     */
    public static final String CLIENT_SECRET = "xxx";
    /**
     * Basic认证 (BASE64编码后的客户端ID和客户端密钥测试用)
     */
    public static final  String BASIC_AUTH = "Basic bWFJQjVuNXVkYWpnRjVnVnNEUFByNU0ycHMxOVJwSGs6eUNuMjdTMFVPZkQ0S3Q1YTQxYnpDT2JhNGl0dnRqMjZqaFlQZEdpb0x4YWZsR3pZZTRKTnZHYTI2NmlwbEZxSQ==";
    /**
     * 回调地址（前端地址）
     */
    public static final String REDIRECT_URI = "http://127.0.0.1:8080/login";
}
