package com.mark.myspring.framework.webmvc.servlet;

import com.mark.myspring.framework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @Author: 帅气的Mark
 * @Description: Mark行行好，给点注释吧！
 * @Date: Create in 2018/4/23 14:47
 * @QQ: 85104982
 */
public class DispatcherServlet extends HttpServlet{
    private static final long serialVersionUID = -7690481559391451803L;

    private  final String LOCATION = "contextConfigLocation";

    @Override
    public void init(ServletConfig config) throws ServletException {
        ApplicationContext context=new ApplicationContext(config.getInitParameter(LOCATION));
    }
}
