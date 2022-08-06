package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class View_03_redirect_RedirectView {

    @RequestMapping("/testRedirect_RedirectView")
    public String testRedirect_RedirectView(){
        //  一般更新提交数据后，更新URL使用重定向，防止刷新页面重复提交
        return "redirect:/hello";    // HTTP 302 - 重定向到"hello" controller - URL改变
    }
}