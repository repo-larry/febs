package com.febs.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @description:
 * @date: 2022/9/18
 **/
@RestController
public class TestController {

    @GetMapping("info")
    public String test(){
        return "febs-server-system";
    }

    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }
}
