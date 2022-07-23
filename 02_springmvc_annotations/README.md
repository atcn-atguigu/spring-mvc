### 三、@RequestMapping注解
#### 1、@RequestMapping注解的功能
从注解名称上我们可以看到，@RequestMapping注解的作用就是将请求和处理请求的控制器方法关联起来，建立映射关系。

SpringMVC 接收到指定的请求，就会来找到在映射关系中对应的控制器方法来处理这个请求。

#### 2、@RequestMapping注解的位置
@RequestMapping标识一个类：设置映射请求的请求路径的初始信息

@RequestMapping标识一个方法：设置映射请求请求路径的具体信息
```java
@Controller
@RequestMapping("/a")   // [@RequestMapping注解的位置] 第一层URI
public class RequestMappingController_02_MultiLayer {

    @RequestMapping("/b")  // [@RequestMapping注解的位置] 第二层URI
    public String multiLayerTest() {
        // 设置视图名称
        return "success";
    }
}
```
```xml
<a th:href="@{/a/b}"> 测试@RequestMapping注解的位置，访问多层级目录URI测试： "/a/b" --> success.html</a><br/>
```
```xml
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>success成功页</title>
</head>
<body>
<h1>成功页</h1>
</body>
</html>
```

#### 3、@RequestMapping注解的value属性
@RequestMapping注解的value属性通过请求的请求地址匹配请求映射

@RequestMapping注解的value属性是一个字符串类型的数组，表示该请求映射能够匹配多个请求地址所对应的请求

@RequestMapping注解的value属性必须设置，至少通过请求地址匹配请求映射
```xml
<a th:href="@{/testRequestMappingValue1}">测试@RequestMapping注解的value属性，多个value值1： "/testRequestMappingValue1" --> success.html</a><br/>
<a th:href="@{/testRequestMappingValue2}">测试@RequestMapping注解的value属性，多个value值2： "/testRequestMappingValue2" --> success.html</a><br/>
```
```java
@Controller
public class RequestMappingController_03_ValueTest {

    @RequestMapping(
            value = {"/testRequestMappingValue1", "/testRequestMappingValue2"}  // 多个value值匹配
    )
    public String testRequestMappingValue() {
        // 设置视图名称
        return "success";
    }
}
```

#### 4、@RequestMapping注解的method属性
@RequestMapping注解的method属性通过请求的请求方式（get或post）匹配请求映射

@RequestMapping注解的method属性是一个RequestMethod类型的数组，表示该请求映射能够匹配多种请求方式的请求

若当前请求的请求地址满足请求映射的value属性，但是请求方式不满足method属性，则浏览器报错405：Request method ‘POST’ not supported
```xml
<a th:href="@{/testRequestMethod}">测试@RequestMapping注解的method属性，多个method值1 GET： "/testRequestMethod，GET" --> success.html</a><br/>
<form th:action="@{/testRequestMethod}" method="post">
<input type="submit" value="测试@RequestMapping注解的method属性，多个method值2：POST: '/testRequestMethod，POST' --> success.html">
</form><br/>
```
```java
@Controller
public class RequestMappingController_04_MethodTest {

    @RequestMapping(
            value = {"/testRequestMethod"},
            method = {RequestMethod.GET, RequestMethod.POST} // method值也可以同时存在多个匹配，默认不设置则支持所有METHOD
    )
    public String testRequestMethod() {
        // 设置视图名称
        return "success";
    }
}
```
```plain/text
注：
1、对于处理指定请求方式的控制器方法，SpringMVC中提供了@RequestMapping的派生注解
处理get请求的映射–>@GetMapping
处理post请求的映射–>@PostMapping
处理put请求的映射–>@PutMapping
处理delete请求的映射–>@DeleteMapping

2、常用的请求方式有get，post，put，delete
但是目前浏览器只支持get和post，若在form表单提交时，为method设置了其他请求方式的字符串（put或delete），则按照默认的请求方式get处理
若要发送put和delete请求，则需要通过spring提供的过滤器HiddenHttpMethodFilter，在RESTful部分会讲到
```

#### 5、@RequestMapping注解的params属性（了解）
@RequestMapping注解的params属性通过请求的请求参数匹配请求映射

@RequestMapping注解的params属性是一个字符串类型的数组，可以通过四种表达式设置请求参数和请求映射的匹配关系

“param”：要求请求映射所匹配的请求必须携带param请求参数

“!param”：要求请求映射所匹配的请求必须不能携带param请求参数

“param=value”：要求请求映射所匹配的请求必须携带param请求参数且param=value

“param!=value”：要求请求映射所匹配的请求必须携带param请求参数但是param!=value

```java
@Controller
@RequestMapping("/testRequestParam")
public class RequestMappingController_05_ParamsTest {
    
    @RequestMapping(
            value = {"/mustHaveParam"},
            method = {RequestMethod.GET},
            params = {"username"}  // 必须携带username参数
    )
    public String mustHaveParam() {
        return "success";
    }

    @RequestMapping(
            value = {"/canNotHaveParam"},
            method = {RequestMethod.GET},
            params = {"!skipLogon"}  // 不能携带skipLogon参数，除skipLogon以外所有其他参数都再匹配规则
    )
    public String canNotHaveParam() {
        return "success";
    }

    @RequestMapping(
            value = {"/paramKeyValueMatch"},
            method = {RequestMethod.GET},
            params = {"user=admin"}  // 参数user=admin
    )
    public String paramKeyValueMatch() {
        return "success";
    }

    @RequestMapping(
            value = {"/paramKeyValueNotMatch"},
            method = {RequestMethod.GET},
            params = {"user!=anonymous"}  // 参数user!=anonymous
    )
    public String paramKeyValueNotMatch() {
        return "success";
    }
}
```
```xml
<h3>@RequestMapping注解的"params"属性 - 写法一</h3>
<a th:href="@{/testRequestParam/mustHaveParam?username=test}">测试@RequestMapping注解的params属性，“param”：必须携带param参数： "/testRequestParam/mustHaveParam?username=test" --> success.html</a><br/>
<a th:href="@{/testRequestParam/canNotHaveParam?skipLogon=true}">测试@RequestMapping注解的params属性，“!param”：不能携带param参数： "/testRequestParam/canNotHaveParam?skipLogon=true" --> success.html(⚠️❌）</a><br/>
<a th:href="@{/testRequestParam/paramKeyValueMatch?user=admin}">测试@RequestMapping注解的params属性，“param=value”：必须携带param请求参数且param=value： "/testRequestParam/paramKeyValueMatch?user=admin" --> success.html</a><br/>
<a th:href="@{/testRequestParam/paramKeyValueNotMatch?user=anonymous}">测试@RequestMapping注解的params属性，“param!=value”：必须携带param请求参数但是param!=value： "/testRequestParam/paramKeyValueNotMatch?user=anonymous" --> success.html(⚠️❌）</a><br/>
<hr/>
<hr/>
<h3>@RequestMapping注解的"params"属性 - 写法二</h3>
<a th:href="@{/testRequestParam/mustHaveParam(username=test)}">测试@RequestMapping注解的params属性，“param”：必须携带param参数： "/testRequestParam/mustHaveParam?username=test" --> success.html</a><br/>
<a th:href="@{/testRequestParam/canNotHaveParam(skipLogon=true)}">测试@RequestMapping注解的params属性，“!param”：不能携带param参数： "/testRequestParam/canNotHaveParam?skipLogon=true" --> success.html(⚠️❌）</a><br/>
<a th:href="@{/testRequestParam/paramKeyValueMatch(user=admin)}">测试@RequestMapping注解的params属性，“param=value”：必须携带param请求参数且param=value： "/testRequestParam/paramKeyValueMatch?user=admin" --> success.html</a><br/>
<a th:href="@{/testRequestParam/paramKeyValueNotMatch(user=anonymous)}">测试@RequestMapping注解的params属性，“param!=value”：必须携带param请求参数但是param!=value： "/testRequestParam/paramKeyValueNotMatch?user=anonymous" --> success.html(⚠️❌）</a><br/>
<hr/>
```
```plain/text
注：
若当前请求满足@RequestMapping注解的value和method属性，但是不满足params属性，此时页面回报错400
e.g. Parameter conditions "!skipLogon" not met for actual request parameters: skipLogon={true}
e.g. Parameter conditions "user!=anonymous" not met for actual request parameters: user={anonymous}
```

### 6、@RequestMapping注解的headers属性（了解）
@RequestMapping注解的headers属性通过请求的请求头信息匹配请求映射

@RequestMapping注解的headers属性是一个字符串类型的数组，可以通过四种表达式设置请求头信息和请求映射的匹配关系

“header”：要求请求映射所匹配的请求必须携带header请求头信息

“!header”：要求请求映射所匹配的请求必须不能携带header请求头信息

“header=value”：要求请求映射所匹配的请求必须携带header请求头信息且header=value

“header!=value”：要求请求映射所匹配的请求必须携带header请求头信息且header!=value

```java
@Controller
@RequestMapping("/testRequestHeader")
public class RequestMappingController_06_HeaderTest {
    
    @RequestMapping(
            value = {"/mustHaveHeader"},
            method = {RequestMethod.GET},
            headers = {"Accept"}  // 必须携带Accept请求头
    )
    public String mustHaveHeader() {
        return "success";
    }

    @RequestMapping(
            value = {"/canNotHaveHeader"},
            method = {RequestMethod.GET},
            headers = {"!Upgrade-Insecure-Requests"}  // 不能携带Upgrade-Insecure-Requests请求头，这个浏览器会发
    )
    public String canNotHaveHeader() {
        return "success";
    }

    @RequestMapping(
            value = {"/headerKeyValueMatch"},
            method = {RequestMethod.GET},
            headers = {"Host=localhost:8080"}  // 请求头Host=localhost:8080
    )
    public String headerKeyValueMatch() {
        return "success";
    }

    @RequestMapping(
            value = {"/headerKeyValueNotMatch"},
            method = {RequestMethod.GET},
            headers = {"Host!=localhost:8080"}  // 请求头Host!=localhost:8080
    )
    public String headerKeyValueNotMatch() {
        return "success";
    }
}
```
```xml
<h3>@RequestMapping注解的"header"属性</h3>
<a th:href="@{/testRequestHeader/mustHaveHeader}">测试@RequestMapping注解的params属性，“param”：必须携带param参数： "/testRequestHeader/mustHaveHeader, Header:Accept（浏览器发）" --> success.html</a><br/>
<a th:href="@{/testRequestHeader/canNotHaveHeader}">测试@RequestMapping注解的params属性，“!param”：不能携带param参数： "/testRequestHeader/canNotHaveHeader, Header:Upgrade-Insecure-Requests（浏览器发）" --> success.html(⚠️❌）</a><br/>
<a th:href="@{/testRequestHeader/headerKeyValueMatch}">测试@RequestMapping注解的params属性，“param=value”：必须携带param请求参数且param=value： "/testRequestHeader/headerKeyValueMatch, Header:Host=localhost:8080（浏览器发）" --> success.html</a><br/>
<a th:href="@{/testRequestHeader/headerKeyValueNotMatch}">测试@RequestMapping注解的params属性，“param!=value”：必须携带param请求参数但是param!=value： "/testRequestHeader/headerKeyValueNotMatch, Header:Host=localhost:8080（浏览器发）" --> success.html(⚠️❌）</a><br/>
<hr/>
```
```plain/text
注：
若当前请求满足@RequestMapping注解的value和method属性，但是不满足headers属性，此时页面显示HTTP Status 404 – Not Found，即资源未找到
请求的资源[/02_springmvc_annotations_war_exploded/testRequestHeader/canNotHaveHeader]不可用
请求的资源[/02_springmvc_annotations_war_exploded/testRequestHeader/headerKeyValueNotMatch]不可用
```

### 7、SpringMVC支持ant风格的路径(模糊匹配)
？：表示任意的单个字符

*：表示任意的0个或多个字符

**：表示任意的一层或多层目录

注意：在使用**时，只能使用/**/xxx的方式
```java
@Controller
public class RequestMappingController_07_ValueRegex {

    // ？：表示任意的单个字符
    @RequestMapping("/a?c/testQuestionMark")
    public String testQuestionMark() {
        return "success";
    }

    // *：表示任意的0个或多个字符
    @RequestMapping("/a*c/testSingleStar")
    public String testSingleStar() {
        return "success";
    }

    // **：表示任意的一层或多层目录，注意：在使用**时，只能使用/**/xxx的方式
    @RequestMapping("/**/testDoubleStar")
    public String testDoubleStar() {
        return "success";
    }
}
```
```xml
<h3>@RequestMapping注解的"value"路径的正则匹配</h3>
<a th:href="@{/a1c/testQuestionMark}">测试@RequestMapping注解的"value"路径的正则匹配，？：表示任意的单个字符： "/a?c/testQuestionMark, /a?c/ -> /a1c/" --> success.html</a><br/>
<a th:href="@{/a12c/testQuestionMark}">测试@RequestMapping注解的"value"路径的正则匹配，？：表示任意的单个字符： "/a?c/testQuestionMark, /a?c/ -> /a12c/" --> success.html(⚠️❌)</a><br/>
<a th:href="@{/a?c/testQuestionMark}">测试@RequestMapping注解的"value"路径的正则匹配，？：表示任意的单个字符： "/a?c/testQuestionMark, /a?c/ -> /a?c/" --> success.html(⚠️❌)</a><br/>
<a th:href="@{/ac/testSingleStar}">测试@RequestMapping注解的"value"路径的正则匹配，*：表示任意的0个或多个字符： "/a*c/testSingleStar, /a*c/ -> /ac/" --> success.html</a><br/>
<a th:href="@{/a1c/testSingleStar}">测试@RequestMapping注解的"value"路径的正则匹配，*：表示任意的0个或多个字符： "/a*c/testSingleStar, /a*c/ -> /a1c/" --> success.html</a><br/>
<a th:href="@{/a12345c/testSingleStar}">测试@RequestMapping注解的"value"路径的正则匹配，*：表示任意的0个或多个字符： "/a*c/testSingleStar, /a*c/ -> /a12345c/" --> success.html</a><br/>
<a th:href="@{/a/testDoubleStar}">测试@RequestMapping注解的"value"路径的正则匹配，**：表示任意的一层或多层目录： "/**/testDoubleStar, /**/ -> /a/" --> success.html</a><br/>
<a th:href="@{/a/b/testDoubleStar}">测试@RequestMapping注解的"value"路径的正则匹配，**：表示任意的一层或多层目录： "/**/testDoubleStar, /**/ -> /a/b/" --> success.html</a><br/>
```

