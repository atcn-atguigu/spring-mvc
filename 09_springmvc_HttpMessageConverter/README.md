### 八、HttpMessageConverter
HttpMessageConverter，报文信息转换器，**将请求报文转换为Java对象**，或**将Java对象转换为响应报文**

HttpMessageConverter提供了两个注解和两个类型：@RequestBody，@ResponseBody，RequestEntity，ResponseEntity
#### 1、@RequestBody - 将请求报文中的请求体转换成java对象
@RequestBody可以获取请求体，需要在控制器方法设置一个形参，使用@RequestBody进行标识，当前请求的请求体就会为当前注解所标识的形参赋值
```html
<h3>1、测试@RequestBody</h3>
<form th:action="@{/testRequestBody}" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    <input type="submit" value="测试@RequestBody">
</form>
```
```java
@Controller
public class HttpController {

    @RequestMapping(value = "/testRequestBody", method = RequestMethod.POST)
    public String testRequestBody(@RequestBody String reqBody) {
        System.out.println(reqBody);
        return "success";
    }
}
```
输出结果：
```plain/text
16:24:44.926 [http-nio-8080-exec-26] DEBUG org.springframework.web.servlet.DispatcherServlet - POST "/09_springmvc_HttpMessageConverter_war_exploded/testRequestBody", parameters={masked}
16:24:44.953 [http-nio-8080-exec-26] DEBUG org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping - Mapped to com.atguigu.mvc.controller.HttpController#testRequestBody(String)
16:24:45.027 [http-nio-8080-exec-26] DEBUG org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor - Read "application/x-www-form-urlencoded;charset=UTF-8" to ["username=root&password=123456"]
username=root&password=123456
```

#### 2、RequestEntity - 请求实体（包含body和headers）

#### 3、@ResponseBody（常用） - 将响应报文中的java对象转换成响应体

#### 4、ResponseEntity（常用） - 响应实体（包含body和headers）