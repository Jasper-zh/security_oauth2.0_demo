package com.hao.auth_server.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
    @RequestMapping("/server")
    public String hello() {
        return "认证服务已登录";
    }
}
