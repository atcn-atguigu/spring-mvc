package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    /**
     * "/" --> http://localhost:8080/01_springmvc_helloworld_war_exploded/WEB-INF/templates/index.html
     * 前缀：/01_springmvc_helloworld_war_exploded/WEB-INF/templates/
     * 后缀：.html
     * @RequestMapping注解：处理请求和控制器方法志坚的映射关系
     * @RequestMapping注解的value属性可以通过请求地址匹配请求,"/"表示的是当前工程的上下文路径
     */
    @RequestMapping("/")
    public String index() {
        // 设置视图名称
        return "index";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "helloWorld";
    }

}
