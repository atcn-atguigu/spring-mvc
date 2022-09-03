package com.atguigu.mvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 等于给该类下所有的方法都加上了（@Controller + @ResponseBody)
public class TestRestController {

    @RequestMapping("/testRestController")
    public String testRestController() {
        return "Test annotation:  @RestController = @Controller + @ResponseBody";
    }
}
