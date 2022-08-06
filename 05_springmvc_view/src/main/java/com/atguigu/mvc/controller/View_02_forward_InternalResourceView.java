package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class View_02_forward_InternalResourceView {

    @RequestMapping("/hello")
    public String hello(){
        return "success";
    }

    @RequestMapping("/testForward_InternalResourceView")
    public String testForward_InternalResourceView(){
        return "forward:/hello";    // HTTP 200 - 转发到"hello" controller，URL不变
    }
}