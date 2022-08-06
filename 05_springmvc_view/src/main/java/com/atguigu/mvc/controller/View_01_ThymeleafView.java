package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class View_01_ThymeleafView {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/testThymeleafView")
    public String testHello(){
        /**
         * 在下面一行代码打断点调试：
         *  1） doDispatch() -> processDispatchResult()
         *      // 在调试Services的Debugger栈堆里找到"doDispatch:1061"，双击打开"DispatchServlet".class类，可下载源码看.java类
         *      // Actually invoke the handler. -- 不管用什么方式return，都最终用ModelAndView对象mv来接收处理
         * 		mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
         * 		// 拦截器中的方法
         * 	    mappedHandler.applyPostHandle(processedRequest, response, mv);
         *
         * 	2）processDispatchResult() -> render()
         * 	    // 处理ModelAndView对象mv（封装的model数据和视图信息）的方法
         * 	    processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
         * 	    // Did the handler return a view to render?
         * 		if (mv != null && !mv.wasCleared()) {
         * 			render(mv, request, response);  // 处理mv对象，请求和响应
         * 			if (errorView) {
         * 				WebUtils.clearErrorRequestAttributes(request);
         *          }
         *      }
         *
         *      // render方法里的解析视图 -- 这里可以看到viewName最终使用的是thymeleaf视图还是其他视图
         *      if (viewName != null) {
         * 			// We need to resolve the view name.
         * 			view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
         * 			if (view == null) {
         * 				throw new ServletException("Could not resolve view with name '" + mv.getViewName() +
         * 						"' in servlet with name '" + getServletName() + "'");
         *                        }* 		}
         *
         */
        return "success"; // 没有前后缀，被springMVC.xml下的视图解析器解析，在thymeleaf视图拼接前后缀
    }
}
