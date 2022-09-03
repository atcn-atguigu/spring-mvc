package com.atguigu.mvc.controller;

import com.atguigu.mvc.pojo.User;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @RequestMapping("/testHttpServletResponse")
    public void testHttpServletResponse(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.getWriter().print("Hello httpServletResponse"); // print的内容作为响应体内容返回给页面
    }

    @ResponseBody
    @RequestMapping("/testResponseBody")
    public String testResponseBody() {
        return "success"; // 返回响应内容"success"，如果没有注解@ResponseBody，则该内容会被认为是视图名称(注意内容字体)
    }

    @ResponseBody
    @RequestMapping("/testResponseUser")
    public User testResponseUser() {
        /**
         * 若直接返回对象，浏览器无法识别返回对象类型（报错：HttpMessageNotWritableException），需要序列化转换成字符串或json才能被浏览器解析
         *
         * 解决方式：
         * 引入jackson-databind依赖，re-import包后，重新启动tomcat（其实4步走，详细看README.md）
         */
        return new User(1001, "admin", "123456", 20, "男");
    }

    @ResponseBody
    @RequestMapping(value = "/testAxios", method = RequestMethod.POST)
    public String testAxios(String username, String password) {
        System.out.println(username + "," + password);
        return "hello axios";
    }
}
