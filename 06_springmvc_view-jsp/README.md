### 五、SpringMVC的视图
#### 5、使用JSP视图解析器替代Thymeleaf视图解析器的写法
在springMVC.xml文件中替换Thymeleaf，使用JSP所用的InternalResourceView视图解析器
```xml
<!-- 配置JSP使用的视图解析器：InternalResourceViewResolver -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <!-- 视图前缀 -->
    <property name="prefix" value="/WEB-INF/templates/"/>
    <!-- 视图后缀 -->
    <property name="suffix" value=".jsp"/>
</bean>
```
```java
@Controller
public class View_01_JSP_InternalResourceView {

    // 注释下面方法，使用视图控制器view-controller配置
//    @RequestMapping("/")
//    public String index() {
//        return "index";
//    }

    @RequestMapping("/testJSPView")
    public String testJSPView(){
        return "success"; // 没有前后缀，被springMVC.xml下的视图解析器解析
    }
}
```
_src/main/webapp/index.jsp_
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>index首页</title>
</head>
<body>
<h1>首页</h1>
<a href="${pageContext.request.contextPath}/testJSPView">测试 "/testJSPView -- return "success";" --> success.html</a><br/>
</body>
</html>
```
_src/main/webapp/WEB-INF/templates/success.jsp_
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>success成功页</title>
</head>
<body>
<h1>成功页</h1>
</body>
</html>
```

```plain/text
注：
当SpringMVC中设置任何一个view-controller时，其他控制器中的请求映射将全部失效，此时需要在SpringMVC的核心配置文件中设置开启mvc注解驱动的标签：
<mvc:annotation-driven />
```