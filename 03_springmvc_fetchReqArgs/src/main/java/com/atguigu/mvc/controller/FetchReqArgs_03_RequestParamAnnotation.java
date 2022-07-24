package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FetchReqArgs_03_RequestParamAnnotation {

    // 前端传过来的请求参数为"user-name"，但形参变量名不允许，引出注解@RequestParam
    @RequestMapping("/testRequestParamTag")
    public String testRequestParamTag(@RequestParam("user-name") String username) {
        System.out.println("username：" + username); // 调用传参数值(user-name=admin)，输出：username：admin
        return "success";
    }

    @RequestMapping("/testRequestParamTagRequired")
    public String testRequestParamTagRequired(@RequestParam(value = "user-name", required = false) String username) {
        System.out.println("username：" + username); // 调用不传参数的输出：username：null
        return "success";
    }

    @RequestMapping("/testRequestParamTagDefaultValue")
    public String testRequestParamTagDefaultValue(@RequestParam(value = "user-name", required = false, defaultValue = "hello") String username) {
        System.out.println("username：" + username); // 调用不传参数的输出：username：hello
        return "success";
    }

    @RequestMapping("/testRequestParamTagDefaultValue2")
    public String testRequestParamTagDefaultValue2(@RequestParam(value = "user-name", required = false, defaultValue = "hello") String username) {
        System.out.println("username：" + username); // 调用传参数(user-name=）的输出：username：hello 【注意：参数值为空，被认为没有传】
        return "success";
    }
}
