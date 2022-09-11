package com.atguigu.mvc.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

// web工程的初始化类，用来代替web.xml
public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    // 指定spring的配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
//        return new Class[0];    // 不使用SpringConfig配置类的话，使用默认数组0；
        return new Class[]{SpringConfig.class};
    }

    // 指定SpringMVC的配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    // 指定DispatcherServlet的映射规则，即url-pattern，返回为数组，可以配置多个
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // 注册过滤器
    // Ctrl + O 重写添加过滤器的方法
    @Override
    protected Filter[] getServletFilters() {
        // 过滤器1： CharacterEncodingFilter
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        // 过滤器2： HiddenHttpMethodFilter
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        return new Filter[]{encodingFilter, hiddenHttpMethodFilter};
    }
}
