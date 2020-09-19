package com.mohit.gcd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginController {

    @GetMapping("")
    public String welcom() {
        return "Good day, Please login";
    }

    @GetMapping("/restricted")
    public Principal login(Principal Principal) {
        return Principal;
    }

    @GetMapping("welcome")
    public String google() {
        return "I am authenticated by google";
    }
}
