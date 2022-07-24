### 四、SpringMVC获取请求参数
#### 1、通过ServletAPI获取
将HttpServletRequest作为控制器方法的形参，此时HttpServletRequest类型的参数表示封装了当前请求的请求报文的对象
```java
@Controller
public class FetchReqArgs_01_OriginServletAPI {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    // 不推荐用法，用了就是侮辱SpringMVC
    @RequestMapping("/testServletAPI")  // 原生API缺陷无法使用URL占位符方式写
    public String testServletAPI(HttpServletRequest httpServletRequest) {    // 使用Servlet原生接口获取当前请求的请求参数
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        System.out.println("username：" + username + "，password：" + password);
        return "success";
    }
}
```
```html
<h3>通过使用原生HttpServletRequest类的getParameter()方法获取请求参数的值</h3>
<a th:href="@{/testServletAPI(username='admin', password=123456)}">测试使用原生Servlet API获取请求参数的值, "/testServletAPI?username=admin&password=123456" --> success.html</a><br/>
```

#### 2、通过控制器方法的形参获取请求参数
在控制器方法的形参位置，设置和请求参数同名的形参，当浏览器发送请求，匹配到请求映射时，在DispatcherServlet中就会将请求参数赋值给相应的形参
```java
@Controller
public class FetchReqArgs_02_MethodParameter {

    // 通过控制器方法的形参来获取参数
    @RequestMapping("/testParameter")
    public String testParameter(String username, String password) {
        System.out.println("username：" + username + "，password：" + password);
        return "success";
    }

    /**
     * ?username=root&password=123456&hobby=running&hobby=swimming
     * 若请求参数中出现多个同名的请求参数，则可以使用字符串接收（hobby：running,swimming），或使用字符串数组接收（hobby：[running, swimming]）
     *
     * 拓展知识点： 如果用Servlet原生方式写法，获取请求参数里的字符串数组值：httpServletRequest.getParameterValues("hobby");
     */
    @RequestMapping("/testParameterMultiValue")
    public String testParameterMultiValue(String username, String password, String hobby) { // String接收字符串数组
        System.out.println("username：" + username + "，password：" + password + "，hobby：" + hobby);
        return "success";
    }

    @RequestMapping("/testParameterStringArray")
    public String testParameterStringArray(String username, String password, String[] hobby) { // String[]接收字符串数组
        System.out.println("username：" + username + "，password：" + password + "，hobby：" + Arrays.toString(hobby));
        return "success";
    }
}
```
```html
<h3>通过使用controller方法的形参来获取请求参数的值</h3>
<a th:href="@{/testParameter(username='admin', password=123456)}">测试使用controller方法的形参接收参数(username, password), "/testParameter(username='admin', password=123456)" --> success.html</a><br/>
<br/>形参显式声明只接收2个parameters，但如果路径匹配下，传入其他味声明的形参值，也同样能处理请求
<form th:action="@{/testParameter}" method="get">
用户名：<input type="text" name="username"><br/>
密码：<input type="password" name="password"><br/>
爱好：跑步<input type="checkbox" name="hobby" value="running">
游泳<input type="checkbox" name="hobby" value="swimming"><br/>
<input type="submit" value="测试使用controller方法的形参接收参数(username, password, 其他：hobby), '/testParameter?username=root&password=123456&hobby=running&hobby=swimming GET' --> success.html">
</form>
<br/>形参使用String接收"字符串数组"，output: username：root，password：123456，hobby：running,swimming
<form th:action="@{/testParameterMultiValue}" method="get">
用户名：<input type="text" name="username"><br/>
密码：<input type="password" name="password"><br/>
爱好：跑步<input type="checkbox" name="hobby" value="running">
游泳<input type="checkbox" name="hobby" value="swimming"><br/>
<input type="submit" value="测试使用controller方法的形参获取请求参数的值, '/testParameterMultiValue?username=root&password=123456&hobby=running&hobby=swimming GET' --> success.html">
</form>
<br/>形参使用String[]接收"字符串数组"，output: username：root，password：123456，hobby：[running, swimming]
<form th:action="@{/testParameterStringArray}" method="get">
用户名：<input type="text" name="username"><br/>
密码：<input type="password" name="password"><br/>
爱好：跑步<input type="checkbox" name="hobby" value="running">
游泳<input type="checkbox" name="hobby" value="swimming"><br/>
<input type="submit" value="测试使用controller方法的形参获取请求参数的值, '/testParameterStringArray?username=root&password=123456&hobby=running&hobby=swimming GET' --> success.html">
</form>
<hr/>
```
```plain/text
注：
若请求所传输的请求参数中有多个同名的请求参数，此时可以在控制器方法的形参中设置字符串数组或者字符串类型的形参接收此请求参数
若使用字符串数组类型的形参，此参数的数组中包含了每一个数据
若使用字符串类型的形参，此参数的值为每个数据中间使用逗号拼接的结果
```

