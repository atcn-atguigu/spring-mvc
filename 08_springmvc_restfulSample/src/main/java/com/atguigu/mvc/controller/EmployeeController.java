package com.atguigu.mvc.controller;

import com.atguigu.mvc.dao.EmployeeDao;
import com.atguigu.mvc.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

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