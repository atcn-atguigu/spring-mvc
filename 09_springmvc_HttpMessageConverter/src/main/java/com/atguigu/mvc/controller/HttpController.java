package com.atguigu.mvc.controller;

import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HttpController {

    @RequestMapping(value = "/testRequestBody", method = RequestMethod.POST)
    public String testRequestBody(@RequestBody String reqBody) {
        System.out.println(reqBody);
        return "success";
    }

    @RequestMapping(value = "/testRequestEntity", method = RequestMethod.POST)
    public String testRequestEntity(RequestEntity<String> requestEntity) {
        System.out.println("Request headers: \n" + requestEntity.getHeaders());
        System.out.println("Request header(referer): \n" + requestEntity.getHeaders().get("Host"));
        System.out.println("Request body: \n" + requestEntity.getBody());
        return "success";
    }
}
