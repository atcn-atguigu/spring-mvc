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
RequestEntity封装请求报文的一种类型，需要在控制器方法的形参中设置该类型的形参，当前请求的请求报文就会赋值给该形参，可以通过getHeaders()获取请求头信息，通过getBody()获取请求体信息
```html
<h3>2、测试RequestEntity</h3>
<form th:action="@{/testRequestEntity}" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    <input type="submit" value="测试RequestEntity">
</form>
<hr/>
```
```java
    @RequestMapping(value = "/testRequestEntity", method = RequestMethod.POST)
    public String testRequestEntity(RequestEntity<String> requestEntity) {
        System.out.println("Request headers: \n" + requestEntity.getHeaders());
        System.out.println("Request header(referer): \n" + requestEntity.getHeaders().get("Host"));
        System.out.println("Request body: \n" + requestEntity.getBody());
        return "success";
    }
```
输出结果：
```plain/text
16:46:28.800 [http-nio-8080-exec-8] DEBUG org.springframework.web.servlet.DispatcherServlet - POST "/09_springmvc_HttpMessageConverter_war_exploded/testRequestEntity", parameters={masked}
16:46:28.804 [http-nio-8080-exec-8] DEBUG org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping - Mapped to com.atguigu.mvc.controller.HttpController#testRequestEntity(RequestEntity)
16:46:28.845 [http-nio-8080-exec-8] DEBUG org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor - Read "application/x-www-form-urlencoded;charset=UTF-8" to ["username=root&password=123456"]
Request headers: 
[host:"localhost:8080", connection:"keep-alive", content-length:"29", cache-control:"max-age=0", sec-ch-ua:"".Not/A)Brand";v="99", "Google Chrome";v="103", "Chromium";v="103"", sec-ch-ua-mobile:"?0", sec-ch-ua-platform:""macOS"", upgrade-insecure-requests:"1", origin:"http://localhost:8080", user-agent:"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36", accept:"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9", sec-fetch-site:"same-origin", sec-fetch-mode:"navigate", sec-fetch-user:"?1", sec-fetch-dest:"document", referer:"http://localhost:8080/09_springmvc_HttpMessageConverter_war_exploded/", accept-encoding:"gzip, deflate, br", accept-language:"en,zh-CN;q=0.9,zh;q=0.8,zh-TW;q=0.7", Content-Type:"application/x-www-form-urlencoded;charset=UTF-8"]
Request header(referer): 
[localhost:8080]
Request body: 
username=root&password=123456
```

#### 3、@ResponseBody（常用） - 将响应报文中的java对象转换成响应体

#### 4、ResponseEntity（常用） - 响应实体（包含body和headers）