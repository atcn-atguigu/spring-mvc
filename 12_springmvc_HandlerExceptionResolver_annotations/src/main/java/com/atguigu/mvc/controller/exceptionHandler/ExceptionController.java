package com.atguigu.mvc.controller.exceptionHandler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // @ControllerAdvice将当前类标识为异常处理的组件
public class ExceptionController {

    @ExceptionHandler(ArithmeticException.class) // @ExceptionHandler用于设置所标识方法处理的异常，对象数组，可用{}指定多个异常
    public String handleArithmeticException(Exception ex, Model model){ // ex表示当前请求处理中出现的异常对象
        model.addAttribute("ex", ex);
        return "error";
    }
}
