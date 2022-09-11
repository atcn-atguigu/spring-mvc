package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestExceptionController {

    @RequestMapping("/testHandlerExceptionResolver")
    public String testHandlerExceptionResolver() {
        System.out.println(1/0); // 此处抛出ArithmeticException异常，用于测试处理异常的视图解析，在springMVC.xml中配置
        return "success";   // 默认没有异常，则跳转到success页面
    }
}
