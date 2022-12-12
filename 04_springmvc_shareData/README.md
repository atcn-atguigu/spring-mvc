### 五、域对象共享数据
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
<h3>1、通过使用原生HttpServletRequest类的setAttribute()方法，向request域对象共享数据</h3>
<a th:href="@{/testScopeOfServletAPI}">测试使用原生Servlet API，值传递返回给视图页面success.html, "/testScopeOfServletAPI" --> success.html</a><br/>
<hr/>
</body>
</html>
```
```html
<h3>1、request域: Servlet API</h3>
<!-- 视图接收Servlet API request域对象共享的数据，thymeleaf只能解析属性上大括号的值，所以不能写在p标签内部 -->
<p th:text="${testServletAPIAttributeData}"></p>
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
<h3>2、request域：Model And View</h3>
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
<h3>3、request域：Model</h3>
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
<h3>4、request域：Map</h3>
<p th:text="${mapData}"></p>
```

#### 5、使用ModelMap向request域对象共享数据
```java
@Controller
public class ObjDataShareScope_05_ModelMap {

    @RequestMapping("/testScopeOfModelMap")
    public String testScopeOfModelMap(ModelMap modelMap) { // 形参传入Model类
        modelMap.addAttribute("modelMapAttributeData", "Hello, Model Map! -- addAttribute()");
        modelMap.put("modelMapPutData", "Hello, Model Map! -- put()");
        return "success";
    }
}
```
```html
<h3>5、使用ModelMap向request域对象共享数据</h3>
<a th:href="@{/testScopeOfModelMap}">测试使用方法形参ModelMap，值传递返回给视图页面success.html, "/testScopeOfModelMap" --> success.html</a><br/>
```
```html
<h3>5、request域：ModelMap</h3>
<p th:text="${modelMapAttributeData}"></p>
<p th:text="${modelMapPutData}"></p>
```

#### 6、Model、ModelMap、Map的关系
Model、ModelMap、Map类型的参数其实本质上都是 BindingAwareModelMap 类型的
```java
public interface Model{}
public class ModelMap extends LinkedHashMap<String, Object> {}
public class ExtendedModelMap extends ModelMap implements Model {}
public class BindingAwareModelMap extends ExtendedModelMap {}
```

#### 7、向session域共享数据
```java
@Controller
public class ObjDataShareScope_07_Session_HttpSession {

    /**
     * Spring MVC为我们提供了注解:
     * @SessionAttribute - 当数据共享在request域的时候，在session域复制一份共享
     * ⚠️ 注意，由于不太好用，所以一般直接使用Servlet API -> HttpSession
     */
    @RequestMapping("/testSession")
    public String testSession(HttpSession session){
        session.setAttribute("testSessionScope", "Hello, HttpSession!");
        return "success";
    }
}
```
```html
<h3>7、Session - 通过Servlet API向session域对象共享数据（推荐使用的原生Servlet API，而不是Spring MVC的注解@SessionAttribute）</h3>
<a th:href="@{/testSession}">测试使用原生Servlet API，值保存与session共享传递返回给视图页面success.html, "/testSession" --> success.html</a><br/>
```
```html
<h3>7、session域：Session(推荐使用的原生Servlet API，而不是Spring MVC的注解@SessionAttribute）</h3>
<!-- 通过session.属性值来获取session的信息 -->
<p th:text="${session.testSessionScope}"></p>
```

#### 8、向application域共享数据
```java
@Controller
public class ObjDataShareScope_08_Application_ServletContext {

    // ServletContext应用范围是整个应用范围
    @RequestMapping("/testApplication")
    public String testApplication(HttpSession session){ // 通过HttpSession获取ServletContext
        ServletContext servletContext = session.getServletContext();
        // ServletContext的几个常用方法：setAttribute()、getAttribute()、removeAttribute()
        servletContext.setAttribute("testApplicationScope", "Hello, ServletContext!");
//        servletContext.getAttribute("testApplicationScope");
//        servletContext.removeAttribute("testApplicationScope");
        return "success";
    }
}
```
```html
<h3>8、Application(ServletContext - 通过Servlet API获取ServletContext向application域对象共享数据</h3>
<a th:href="@{/testApplication}">测试使用原生Servlet API，值保存于application共享传递返回给视图页面success.html, "/testApplication" --> success.html</a><br/>
```
```html
<h3>8、application域；application</h3>
<!-- 通过application.属性值来获取application的信息 -->
<p th:text="${application.testApplicationScope}"></p>
```


#### 9、@RequestAttribute形参读取请求域中的数据
```java
@Controller
public class ObjDataShareScope_09_Annotations_RequestAttribute {

    // ServletContext应用范围是整个应用范围
    @GetMapping("/testAnnotationRequestAttribute")
    public String testAnnotationRequestAttribute(HttpServletRequest httpServletRequest) {
        httpServletRequest.setAttribute("msg", "成功");
        httpServletRequest.setAttribute("code", "200");
        // 为了传递请求域中的属性值，这里使用forward转发，在另一个方法里使用@RequestAttribute获取请求域中的属性值
        return "forward:/readRequestAttribute";
    }

    @GetMapping("/readRequestAttribute")
    public String readAttribute(@RequestAttribute("msg") String msg,
                                @RequestAttribute("code") Integer code,
                                HttpServletRequest httpServletRequest) {
        // 使用Map返回给浏览器，更方便打印更多数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("context_msg", msg);
        map.put("context_code", code);
        System.out.println("@RequestAttribute注解读取的值为：" + map);
        // request域共享数据，渲染视图success页展示数据
        httpServletRequest.setAttribute("readRequestAttributeScope", map);
        return "success";
    }
}
```
```html
<h3>9、@RequestAttribute获取request域对象共享数据</h3>
<a th:href="@{/testAnnotationRequestAttribute}">测试使用方法形参使用 @RequestAttribute("k") String v，获取request域共享属性值，值传递返回, "/testAnnotationRequestAttribute" forward "/readRequestAttribute"</a><br/>
```
```html
<h3>9、request域：@RequestAttribute</h3>
<p th:text="${readRequestAttributeScope}"></p>
```