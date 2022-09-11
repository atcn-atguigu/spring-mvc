package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InterceptorController {

    @RequestMapping("/**/testInterceptor") // 配置成支持一级或多级目录，如：/testInterceptor、/a/testInterceptor 或 /a/b/testInterceptor
    public String testInterceptor() {
        return "success";
    }
}
