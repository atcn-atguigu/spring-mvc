### 四、域对象共享数据
#### 1、使用ServletAPI向request域对象共享数据
```java
@Controller
public class ObjDataShareScope_01_OriginServletAPI {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    // 使用servlet API向request域对象共享数据
    @RequestMapping("/testScopeOfServletAPI")
    public String testServletAPI(HttpServletRequest httpServletRequest) {    // 使用Servlet原生接口获取当前请求的请求参数
        // Servlet API 域共享数据主要有3个方法：
        // （1）setAttribute()；
        httpServletRequest.setAttribute("testServletAPIAttributeData", "Hello, servlet API!!");
        //（2）getAttribute()；
        System.out.println("使用Servlet原生API设置数据共享scope，属性'servletAPIAttributeData'：" + httpServletRequest.getAttribute("testServletAPIAttributeData"));
        //（3）removeAttribute()
//        httpServletRequest.removeAttribute("testServletAPIAttributeData");
        return "success";   // 请求转发给success视图页面，相应servlet request域对象数据也为之共享
    }
}
```
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>index首页</title>
</head>
<body>
<h1>首页</h1>
<h3>1. 通过使用原生HttpServletRequest类的setAttribute()方法，向request域对象共享数据</h3>
<a th:href="@{/testScopeOfServletAPI}">测试使用原生Servlet API，值传递返回给视图页面success.html, "/testScopeOfServletAPI" --> success.html</a><br/>
<hr/>
</body>
</html>
```
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>success成功页</title>
</head>
<body>
<h1>成功页</h1>
<!-- 视图接收Servlet API request域对象共享的数据，thymeleaf只能解析属性上大括号的值，所以不能写在p标签内部 -->
<p th:text="${testServletAPIAttributeData}"></p>
</body>
</html>
```
#### 2、使用ModelAndView向request域对象共享数据
```java
@Controller
public class ObjDataShareScope_02_ModelAndView {

    @RequestMapping("/testScopeOfModelAndView")
    public ModelAndView testScopeOfModelAndView() {
        ModelAndView modelAndView = new ModelAndView();
        // 处理模型数据，即是向request域共享数据。
        modelAndView.addObject("modelAndViewScopeData", "Hello, Model and View!");
        // 设置视图名称
        modelAndView.setViewName("success");
        return modelAndView;    // modelAndView对象必须作为方法返回值
    }
}
```
```html
<h3>2、使用ModelAndView向request域对象共享数据</h3>
<a th:href="@{/testScopeOfModelAndView}">测试使用ModelAndView类，值传递返回给视图页面success.html, "/testScopeOfModelAndView" --> success.html</a><br/>
```
```html
<h3>2、Model And View</h3>
<p th:text="${modelAndViewScopeData}"></p>
```

#### 3、使用Model向request域对象共享数据
```java
@Controller
public class ObjDataShareScope_03_Model {

    @RequestMapping("/testScopeOfModel")
    public String testScopeOfModel(Model model) { // 形参传入Model类
        model.addAttribute("modelAttributeData", "Hello, Model!");
        return "success";
    }
}
```
```html
<h3>3、使用Model向request域对象共享数据</h3>
<a th:href="@{/testScopeOfModel}">测试使用方法形参Model，值传递返回给视图页面success.html, "/testScopeOfModel" --> success.html</a><br/>
```
```html
<h3>3、Model</h3>
<p th:text="${modelAttributeData}"></p>
```

#### 4、使用map向request域对象共享数据
```java
@Controller
public class ObjDataShareScope_04_Map {

    @RequestMapping("/testScopeOfMap")
    public String testScopeOfMap(Map<String, Object> map) {
        map.put("mapData", "Hello Map!");
        return "success";
    }
}
```
```html
<h3>4、使用map向request域对象共享数据</h3>
<a th:href="@{/testScopeOfMap}">测试使用方法形参Map，值传递返回给视图页面success.html, "/testScopeOfMap" --> success.html</a><br/>
```
```html
<h3>4、Map</h3>
<p th:text="${mapData}"></p>
```


