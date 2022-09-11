### 十一、异常处理器
#### 2、基于注解的异常处理（annotations）
注释掉xml配置，使用注解形式配置异常处理 -- 其他代码同xml项目一样不变
```java
@ControllerAdvice // @ControllerAdvice将当前类标识为异常处理的组件
public class ExceptionController {

    @ExceptionHandler(ArithmeticException.class) // @ExceptionHandler用于设置所标识方法处理的异常，对象数组，可用{}指定多个异常
    public String handleArithmeticException(Exception ex, Model model){ // ex表示当前请求处理中出现的异常对象
        model.addAttribute("ex", ex);
        return "error";
    }
}
```