### 八、RESTFul案例

#### 1、准备工作

REST：Representational State Transfer，表现层资源状态转移。

和传统 CRUD 一样，实现对员工信息的增删改查。

搭建环境，准备实体类
```java
package com.atguigu.mvc.pojo;

public class Employee {

    private Integer id;
    private String lastName;
    private String email;
    //1 male, 0 female
    private Integer gender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Employee(Integer id, String lastName, String email, Integer gender) {
        super();
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                '}';
    }
}
```
EmployeeDao类的静态Map对象来模拟数据库的表
```java
package com.atguigu.mvc.dao;

import com.atguigu.mvc.pojo.Employee;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EmployeeDao {

    private static Map<Integer, Employee> employees = null;

    static {
        employees = new HashMap<Integer, Employee>();
        employees.put(1001, new Employee(1001, "E-AA", "aa@163.com", 1));
        employees.put(1002, new Employee(1002, "E-BB", "bb@163.com", 1));
        employees.put(1003, new Employee(1003, "E-CC", "cc@163.com", 0));
        employees.put(1004, new Employee(1004, "E-DD", "dd@163.com", 0));
        employees.put(1005, new Employee(1005, "E-EE", "ee@163.com", 1));
    }

    private static Integer initId = 1006;

    // 实现添加和修改
    public void save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(initId++);   // 先使用当前1006，再自增
        }
        employees.put(employee.getId(), employee);
    }

    // 查询所有
    public Collection<Employee> getAll() {
        return employees.values();
    }

    // 查询某个
    public Employee get(Integer id) {
        return employees.get(id);
    }

    // 删除
    public void delete(Integer id) {
        employees.remove(id);
    }
}
```

#### 2、功能清单

| 功能         | URL 地址         | 请求方式   |
|------------|----------------|--------|
| 访问首页√      | /              | GET    |
| 查询全部数据√    | /employee      | GET    |
| 删除√        | /employee/1002 | DELETE |
| 跳转到添加数据页面√ | /toAdd         | GET    |
| 执行保存√      | /employee      | POST   |
| 跳转到更新数据页面√ | /employee/2    | GET    |
| 执行更新√      | /employee      | PUT    |


#### 3、增删改查实现（删除使用Vue处理点击事件）
EmployeeController - 控制器实现逻辑处理，视图页面跳转，重定向controller方法
```java
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    // 查询所有 - 练习使用ModelAndView写法
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    private ModelAndView getAllEmployees() {
        Collection<Employee> employees = employeeDao.getAll();
        ModelAndView mv = new ModelAndView();
        mv.addObject("allEmployeesData", employees);
        mv.setViewName("employee_list");    // 跳转到员工列表视图
        return mv;
    }

    // 查询某个 - 练习使用Model写法
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    private String getEmployeeById(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeDao.get(id);
        model.addAttribute("employeeByIdData", employee);
        return "employee_update";    // 跳转到员工修改视图
    }

    // 删除
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE)
    private String removeEmployeeById(@PathVariable("id") Integer id) {
        employeeDao.delete(id);
        return "redirect:/employee"; // 重定向回到查询所有（默认GET请求展示所有员工table最新数据）
    }

    // 修改
    @RequestMapping(value = "/employee", method = RequestMethod.PUT)
    private String updateEmployee(Employee employee) {
        employeeDao.save(employee);
        return "redirect:/employee"; // 重定向回到查询所有（默认GET请求展示所有员工table最新数据）
    }

    // 添加
    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    private String addEmployee(Employee employee) {
        employeeDao.save(employee);
        return "redirect:/employee"; // 重定向回到查询所有（默认GET请求展示所有员工table最新数据）
    }

}
```
springMVC.xml - view-controller配置GET类型视图控制
```xml
<mvc:view-controller path="/" view-name="index"></mvc:view-controller>
<mvc:view-controller path="/toAdd" view-name="employee_add"></mvc:view-controller>
```
index.html - 首页，包含几个增删改查基础操作
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>index首页</title>
</head>
<body>
<h1>首页</h1>
<h3>1、查询所有员工信息：    GET /employee</h3>
<a th:href="@{/employee}">查询所有员工信息 "GET /employee" --> employee_list.html</a>
<hr/>

<h3>2、根据id查询员工信息：  GET /employee/{id}</h3>
<a th:href="@{/employee/1001}">根据id查询员工信息 "GET /employee/1001" --> employee_list.html</a>
<hr/>

<h3>3、根据id删除员工信息：  DELETE /employee/1002</h3>
<form th:action="@{/employee/1002}" method="post">
    <input type="hidden" name="_method" value="DELETE">
    <input type="submit" value="删除员工信息">
</form>
<hr/>

<h3>4、修改员工信息：       PUT /employee</h3>
<form th:action="@{/employee}" method="post">   <!-- form 表单仅支持get和post，写成其他仍为默认get-->
    <input type="hidden" name="_method" value="PUT">    <!-- 这行能触发HiddenHttpMethodFilter来发送PUT请求 -->
    *id：<input type="text" name="id"><br>
    lastName：<input type="text" name="lastName"><br>
    email：<input type="text" name="email"><br>
    gender：<input type="radio" name="gender" value="1">Male
            <input type="radio" name="gender" value="0">Female<br>
    <input type="submit" value="修改员工信息">
</form>

<h3>5、添加员工信息：       POST /employee</h3>
<form th:action="@{/employee}" method="post">
    lastName：<input type="text" name="lastName"><br>
    email：<input type="text" name="email"><br>
    gender：<input type="radio" name="gender" value="1">Male
    <input type="radio" name="gender" value="0">Female<br>
    <input type="submit" value="添加员工信息">
</form>
<hr/>
</body>
</html>
```
employee_list.html - 员工列表展示（使用Vue.js来处理删除员工按钮点击事件）
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>success成功页</title>
</head>
<body>
<h1>成功页</h1>
<h3>1、查询所有员工信息：    GET /employee</h3>
<h4>写法 1 - 直接展示员工数据</h4>
<p th:text="${allEmployeesData}"></p>   <!-- 直接展示mv属性对象 -->
<h4>写法 2 - 使用table展示员工数据</h4>
<table id="dataTable" border="1" cellspacing="0" cellpadding="0" style="text-align: center">
    <tr>
        <th colspan="5">Employee Info</th>  <!-- 表头行1 -->
    </tr>
    <tr>
        <th>id</th>       <!-- 表头行2 -->
        <th>lastName</th>
        <th>email</th>
        <th>gender</th>
        <th>options（<a th:href="@{/toAdd}">添加员工</a>）</th>   <!-- 添加员工按钮，试图控制在springMVC.xml配置 -->
    </tr>
    <tr th:each="employee : ${allEmployeesData}">   <!-- 用thymeleaf遍历数据（mv属性对象） -->
        <td th:text="${employee.id}"></td>
        <td th:text="${employee.lastName}"></td>
        <td th:text="${employee.email}"></td>
        <td th:text="${employee.gender}"></td>
        <td>
            <a @click="deleteEmployee" th:href="@{'/employee/'+${employee.id}}">delete</a>  <!-- 写法2：点击事件 -->
            <!-- 2、根据id查询员工信息： GET /employee/1001，控制器里控制视图跳转到employee_update.html页面来处理修改 -->
            <a th:href="@{'/employee/'+${employee.id}}">update</a>
        </td>
    </tr>
</table>
<form id="deleteForm" method="post">    <!-- table对应delete方法 -->
    <input type="hidden" name="_method" value="delete">
</form>
<hr/>

<h3>3、根据id删除员工信息：  DELETE /employee/1002 -- 更新结果看1的表格数据</h3>
<hr/>
<h3>4、修改员工信息：       PUT /employee -- 更新结果看1的表格数据</h3>
<hr/>


<!-- Vue 配置用于超链接事件处理-->
<script type="text/javascript" th:src="@{/static/js/vue.js}"></script>
<script type="text/javascript">
    var vue = new Vue({
        el: "#dataTable", // Vue绑定页面table元素
        methods: {
            // 删除员工的超链接，点击事件触发的方法"deleteEmployee"
            deleteEmployee:function(event) {
                // 根据id获取form元素
                let deleteForm = document.getElementById("deleteForm");
                // 将触发点击事件的超链接的href，属性赋值给form表单的action
                deleteForm.action = event.target.href;
                deleteForm.submit(); // 提交表单
                /**
                 * html部分标签带有默认行为，
                 * 例如超链接：点击之后，就算有绑定的点击事件，它仍然会跳转页面。
                 * 例如form表单：点击submit之后，先执行点击事件，再提交表单。
                 */
                event.preventDefault(); // 阻止超链接的默认跳转行为
            }
        }
    })
</script>
</body>
</html>
```
employee_add.html - 添加员工视图
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>添加员工</title>
</head>
<body>
<h3>5、添加员工信息：       POST /employee</h3>
<form th:action="@{/employee}" method="post">
    lastName：<input type="text" name="lastName"><br>
    email：<input type="text" name="email"><br>
    gender：<input type="radio" name="gender" value="1">Male
    <input type="radio" name="gender" value="0">Female<br>
    <input type="submit" value="添加员工信息">
</form>
</body>
</html>
```
employee_update.html - 更新员工视图
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>修改员工</title>
</head>
<body>

<h3>2、根据id查询员工信息：  GET /employee/{id}</h3>
<p th:text="${employeeByIdData}"></p>
<hr/>

<h3>4、修改员工信息：       PUT /employee</h3>
<form th:action="@{/employee}" method="post">   <!-- form 表单仅支持get和post，写成其他仍为默认get-->
    <input type="hidden" name="_method" value="PUT">    <!-- 这行能触发HiddenHttpMethodFilter来发送PUT请求 -->
    <input type="hidden" name="id" th:value="${employeeByIdData.id}">   <!-- id值不变，所以使用原来的，不显示修改 -->
    lastName：<input type="text" name="lastName" th:value="${employeeByIdData.lastName}"><br>
    email：<input type="text" name="email" th:value="${employeeByIdData.email}"><br>
    <!-- 使用 th:field 来回显radio已经选择的数值 -->
    gender：<input type="radio" name="gender" value="1" th:field="${employeeByIdData.gender}">Male
    <input type="radio" name="gender" value="0" th:field="${employeeByIdData.gender}">Female<br>
    <input type="submit" value="修改员工信息">
</form>
</body>
</html>
```