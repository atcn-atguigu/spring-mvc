### 十二、注解配置SpringMVC
使用配置类和注解代替web.xml和SpringMVC配置文件的功能
#### 1、创建初始化类，代替web.xml
在**Servlet3.0**环境中，容器会在类路径中查找实现javax.servlet.ServletContainerInitializer接口的类，如果找到的话就用它来配置Servlet容器。

Spring提供了这个接口的实现，名为SpringServletContainerInitializer，这个类反过来又会查找实现WebApplicationInitializer的类并将配置的任务交给它们来完成。
Spring3.2引入了一个便利的WebApplicationInitializer基础实现，名为AbstractAnnotationConfigDispatcherServletInitializer，当我们的类扩展了AbstractAnnotationConfigDispatcherServletInitializer并将其部署到Servlet3.0容器的时候，容器会自动发现它，并用它来配置Servlet上下文。
```java
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
```

#### 2、创建SpringConfig配置类，代替spring的配置文件
```java
@Configuration  // 将当前类标识为一个配置类
public class SpringConfig {
    //ssm整合之后，spring的配置信息写在此类中
}
```

#### 3、创建WebConfig配置类，代替SpringMVC的配置文件
```java
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
```