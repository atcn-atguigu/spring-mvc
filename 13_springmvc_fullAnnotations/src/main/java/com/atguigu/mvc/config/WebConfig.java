package com.atguigu.mvc.config;

import com.atguigu.mvc.interceptors.FirstInterceptor;
import com.atguigu.mvc.interceptors.SecondInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;
import java.util.Properties;

/*  代替SpringMVC的配置文件
    1）扫描组件
    2）开启mvc注解驱动
    3）default-servlet-handler使用默认的servlet处理静态资源
    4）视图解析器
    5）view-controller
    6）文件上传下载
    7）拦截器
    8）异常处理
 */
@Configuration  // 将当前类标识为一个配置类
@ComponentScan(basePackages = {"com.atguigu.mvc"})  // 1）扫描组件
@EnableWebMvc   // 2）开启mvc注解驱动
public class WebConfig implements WebMvcConfigurer {

    /**
     * 3）default-servlet-handler使用默认的servlet处理静态资源
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * 4）视图解析器：3步走创建Thymeleaf视图解析器
     */
    // 配置生成模板解析器第一步：ITemplateResolver
    @Bean   // ⚠️注意：这个是Spring托管的@Bean注解，返回值为IoC容器中的bean
    public ITemplateResolver templateResolver() {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        // ServletContextTemplateResolver需要一个ServletContext作为构造参数，可通过WebApplicationContext 的方法获得
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(
                webApplicationContext.getServletContext());
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    // 配置生成模板解析器第二步：SpringTemplateEngine
    //生成模板引擎并为模板引擎注入模板解析器
    @Bean   // ⚠️注意：这个是Spring托管的@Bean注解，返回值为IoC容器中的bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {    // 参数为IoC的bean，自动装配
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    // 配置生成模板解析器第三步：ViewResolver
    //生成视图解析器并未解析器注入模板引擎
    @Bean   // ⚠️注意：这个是Spring托管的@Bean注解，返回值为IoC容器中的bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {    // 参数为IoC的bean，自动装配
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setTemplateEngine(templateEngine);
        return viewResolver;
    }

    /**
     *  5）view-controller视图控制
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");   // 首页
        registry.addViewController("/file").setViewName("file");    // 文件上传下载页面
    }

    /**
     *  6）文件上传下载
     */
    @Bean   // ⚠️注意：这个是Spring托管的@Bean注解，返回值为IoC容器中的bean
    public CommonsMultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }

    /**
     *  7）拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        FirstInterceptor firstInterceptor = new FirstInterceptor();
        SecondInterceptor secondInterceptor = new SecondInterceptor();
        registry.addInterceptor(firstInterceptor).addPathPatterns("/**");
        registry.addInterceptor(secondInterceptor).addPathPatterns("/**").excludePathPatterns("/");
    }

    /**
     * 8）异常处理
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        Properties prop = new Properties();
        prop.setProperty("java.lang.ArithmeticException", "error");
        //设置异常映射
        exceptionResolver.setExceptionMappings(prop);
        //设置共享异常信息的键
        exceptionResolver.setExceptionAttribute("ex");
        resolvers.add(exceptionResolver);
    }

}