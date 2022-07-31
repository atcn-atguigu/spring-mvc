package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ObjDataShareScope_03_Model {

    @RequestMapping("/testScopeOfModel")
    public String testScopeOfModel(Model model) { // 形参传入Model类
        model.addAttribute("modelAttributeData", "Hello, Model!");
        return "success";
    }
}
