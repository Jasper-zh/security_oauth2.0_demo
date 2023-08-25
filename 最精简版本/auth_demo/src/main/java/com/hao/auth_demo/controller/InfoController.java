package com.hao.auth_demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @RequestMapping("/info")
    public Object info(Authentication authentication) {
        return authentication;
    }

    @RequestMapping("/admin")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String admin() {
        return "管理员才看的到这条消息";
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('admin','add')")
    public String add() {
        return "添加了一个商品";
    }
}
