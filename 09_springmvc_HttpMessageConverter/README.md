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
@ResponseBody用于标识一个控制器方法，可以将该方法的返回值直接作为响应报文的响应体响应到浏览器

其他方法：除了使用SpringMVC的注解实现外，也可以用Servlet原生类HttpServletResponse来处理。
##### 3.1）Servlet原生类HttpServletResponse来处理
```html
<h3>3.1、测试HttpServletResponse</h3>
<a th:href="@{/testHttpServletResponse}">通过servlet API的response对象响应浏览器数据</a>
<hr/>
```
```java
    @RequestMapping(value = "/testHttpServletResponse")
    public void testHttpServletResponse(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.getWriter().print("Hello httpServletResponse"); // print的内容作为响应体内容返回给页面
    }
```

##### 3.2.1）SpringMVC的@ResponseBody注解来处理(字符串)
```html
<h3>3.2.1、测试@ResponseBody</h3>
<a th:href="@{/testResponseBody}">/testResponseBody -> 通过SpringMVC的@ResponseBody注解，响应浏览器数据</a>
<hr/>
```
```java
    @ResponseBody
    @RequestMapping(value = "/testResponseBody")
    public String testResponseBody() throws IOException {
        return "success"; // 返回响应内容"success"，如果没有注解@ResponseBody，则该内容会被认为是视图名称(注意内容字体)
    }
```

##### 3.2.2）SpringMVC的@ResponseBody注解来处理(json)
```xml
<h3>3.2.2、测试@ResponseBody（响应对象：json对象或json数组）</h3>
<a th:href="@{/testResponseUser}">/testResponseUser -> 通过SpringMVC的@ResponseBody注解，响应浏览器数据（对象：json对象或json数组）</a>
<hr/>
```
```java
    @ResponseBody
    @RequestMapping(value = "/testResponseUser")
    public User testResponseUser() {
        /**
         * 若直接返回对象，浏览器无法识别返回对象类型（报错：HttpMessageNotWritableException），需要序列化转换成字符串或json才能被浏览器解析
         *
         * 解决方式：
         * 引入jackson-databind依赖，re-import包后，重新启动tomcat（其实4步走，详细看README.md）
         */
        return new User(1001, "admin", "123456", 20, "男");
    }
```
@ResponseBody处理json的步骤：

a> 导入jackson的依赖
```xml
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.4</version>
        </dependency>
```
b> 在SpringMVC的核心配置文件中开启mvc的注解驱动，此时在HandlerAdaptor中会自动装配一个消息转换器：MappingJackson2HttpMessageConverter，可以将响应到浏览器的Java对象转换为Json格式的字符串
```xml
<mvc:annotation-driven/>
```
c> 在处理器方法上使用@ResponseBody注解进行标识

d> 将Java对象直接作为控制器方法的返回值返回，就会自动转换为Json格式的字符串（java代码如上）

浏览器页面中展示的结果：
```json
{
    id: 1001,
    username: "admin",
    password: "123456",
    age: 20,
    sex: "男"
}
```

##### 3.2.3）SpringMVC的@ResponseBody注解来处理(ajax)
a>请求超链接：
```html
<h3>3.2.3、测试@ResponseBody（响应ajax）</h3>
<div id="app">
    <a @click="testAjax" th:href="@{/testAxios}">/testAxios -> 通过SpringMVC的@ResponseBody注解，响应浏览器数据（ajax）</a>
</div>
```
b>通过vue和axios处理点击事件：
```html
<script type="text/javascript" th:src="@{/static/js/vue.js}"></script>
<script type="text/javascript" th:src="@{/static/js/axios.min.js}"></script>
<script type="text/javascript">
    new Vue({
        el: "#app",
        methods: {
            // a标签点击事件函数
            testAjax: function (event) {
                axios({
                    method: "post",         // 请求method
                    url: event.target.href, // 请求URL（从a标签的href属性获取）
                    params: {               // 请求参数
                        username: "admin",
                        password: "123456"
                    }
                }).then(
                    // Ajax请求成功之后处理的函数
                    function (response) {
                        alert(response.data)    // 响应数据，在alert弹窗中显示
                    })
                event.preventDefault()  // 阻止页面跳转
            }
        }
    })
</script>
<hr/>
```
c>控制器方法：
```java
    @ResponseBody
    @RequestMapping(value = "/testAxios", method = RequestMethod.POST)
    public String testAxios(String username, String password) {
        System.out.println(username + "," + password);
        return "hello axios";
    }
```

##### @RestController注解
@RestController注解是springMVC提供的一个复合注解，标识在控制器的类上，就相当于为类添加了@Controller注解，并且为其中的每个方法添加了@ResponseBody注解
```html
<h3>3.3、测试@RestController注解（@Controller + @ResponseBody）</h3>
<a th:href="@{/testRestController}">/testRestController -> @RestController = @Controller + @ResponseBody</a>
<hr/>
```
```java
@RestController // 等于给该类下所有的方法都加上了（@Controller + @ResponseBody)
public class TestRestController {

    @RequestMapping("/testRestController")
    public String testRestController() {
        return "Test annotation:  @RestController = @Controller + @ResponseBody";
    }
}
```

#### 4、ResponseEntity（常用） - 响应实体（包含body和headers）
ResponseEntity用于控制器方法的返回值类型，该控制器方法的返回值就是响应到浏览器的响应报文(可以自定义响应报文) - 查看文件下载例子

### 九、文件上传和下载
#### 1、文件下载
使用**ResponseEntity**实现下载文件的功能

springMVC.xml 视图控制配置
```xml
<mvc:view-controller path="/file" view-name="file"></mvc:view-controller>
```
index.html
```html
<h3>4、测试使用ResponseEntity实现下载文件的功能</h3>
<a th:href="@{/file}">跳转到文件下载</a>
<hr/>
```
file.html
```html
<h3>1、文件下载</h3>
<a th:href="@{/testDownload}">下载1.jpg</a>
<hr/>
```
```java
@Controller
public class FileDownloadAndUpload {

    @RequestMapping("/testDownload")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException {
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/static/img/1.jpg"); // servlet上下文getRealPath() - 获取服务器部署路径
        //创建输入流
        InputStream is = new FileInputStream(realPath);
        //创建字节数组
        byte[] bytes = new byte[is.available()];    // 输入流available() - 创建长度为文件对应的字节数大小
        //将流读到字节数组中
        is.read(bytes);
        //创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename=1.jpg");    // 响应报文header，可设置下载的默认文件名
        //设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode); // ResponseEntity<byte[]> 响应报文类型
        //关闭输入流
        is.close();
        return responseEntity;
    }
}
```

#### 2、文件上传
文件上传要求form表单的请求方式必须为post，并且添加属性enctype=“multipart/form-data”

SpringMVC中将上传的文件封装到MultipartFile对象中，通过此对象可以获取文件相关信息

上传步骤：

a> 添加依赖：
```xml
<!-- 文件上传依赖 -->
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
</dependency>
```
b> 在SpringMVC的配置文件中添加配置：
```xml
    <!-- 必须通过文件解析器的解析才能将文件转换为MultipartFile对象 -->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"></bean>
```
c> 控制器方法：
```java
    @RequestMapping("/testUpload")
    public String testUp(MultipartFile uploadFile, HttpSession session) throws IOException {
        String name = uploadFile.getName(); // 获取上传文件的key名：uploadFile
        //获取上传的文件的文件名
        String fileName = uploadFile.getOriginalFilename();
        //处理文件重名问题，若不处理，则文件会被覆盖写入
        String hzName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString() + hzName;   // 防止上传文件重名，使用UUID
        //获取服务器中photo目录的路径
        ServletContext servletContext = session.getServletContext();
        String uploadFilePath = servletContext.getRealPath(name);   // getRealPath部署路径下的上传文件夹名称，此处使用"uploadFile"
        File file = new File(uploadFilePath);
        if(!file.exists()){
            file.mkdir();   // 若"uploadFile"文件夹目录不存在，则创建一个
        }
        String finalPath = uploadFilePath + File.separator + fileName;
        //实现上传功能
        uploadFile.transferTo(new File(finalPath)); // 拷贝文件上传到服务器目录
        return "success";
    }
```
d> file.html
```html
<h3>2、文件上传</h3>
<form th:action="@{/testUpload}" method="post" enctype="multipart/form-data">
    上传文件：<input type="file" name="uploadFile"><br>
    <input type="submit" value="上传">
</form>
<hr/>
```
