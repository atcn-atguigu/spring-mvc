package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class View_01_JSP_InternalResourceView {

    // 注释下面方法，使用视图控制器view-controller配置
//    @RequestMapping("/")
//    public String index() {
//        return "index";
//    }

    @RequestMapping("/testJSPView")
    public String testJSPView(){
        return "success"; // 没有前后缀，被springMVC.xml下的视图解析器解析
    }
}
