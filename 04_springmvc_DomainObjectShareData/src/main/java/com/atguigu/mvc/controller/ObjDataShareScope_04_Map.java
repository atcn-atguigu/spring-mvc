package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class ObjDataShareScope_04_Map {

    @RequestMapping("/testScopeOfMap")
    public String testScopeOfMap(Map<String, Object> map) {
        map.put("mapData", "Hello Map!");
        return "success";
    }
}
