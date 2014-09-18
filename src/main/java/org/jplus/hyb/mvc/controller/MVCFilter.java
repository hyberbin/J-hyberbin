package org.jplus.hyb.mvc.controller;

import org.jplus.annotation.Before;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.log.LocalLogger;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.hyb.mvc.bean.MVCBean;
import org.jplus.hyb.mvc.classloader.ClassScanner;
import org.jplus.hyb.mvc.classloader.IClassLoader;
import org.jplus.util.ConverString;
import org.jplus.util.NumberUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import org.jplus.hyb.database.config.DbConfig;

/**
 * MVCFilter说明.
 * @author Hyberbin
 * @date 2013-6-8 11:45:29
 */
@WebFilter(filterName = "Filter", urlPatterns = {"/*"})
public class MVCFilter implements Filter {
    private final static Logger log=LoggerManager.getLogger(MVCFilter.class);
    private static IClassLoader classLoader= ClassScanner.MY_INSTANCE;

    /**
     * 初始化过滤器
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LocalLogger.setLevel(NumberUtils.parseInt(filterConfig.getInitParameter("level")));
        LoggerManager.setLogFactory(filterConfig.getInitParameter("loggerFactory"));
        ConfigCenter.INSTANCE.setManager("org.jplus.hyb.database.transaction.AutoManager",DbConfig.DEFAULT_CONFIG_NAME);
    }

    /**
     * 执行过滤器
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setCharacterEncoding("utf-8");
        res.setCharacterEncoding("utf-8");
        HttpSession session = req.getSession();
        String url = req.getRequestURI();
        String contexPathString = req.getContextPath();
        if (url == null) {
            chain.doFilter(request, response);
            return;
        }
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        url = url.replace(contexPathString, "").replace("//", "/");
        doGetFilter(req);
        MVCBean mvcMehod = classLoader.getActionClass(url);
        if (mvcMehod == null) {
            chain.doFilter(request, response);
            return;
        }
        MVCBean before = classLoader.getActionBefore(url);
        MVCBean after = classLoader.getActionAfter(url);
        String send;
        if (before != null) {//如果一个Action有准备方法先调用准备方法
            log.debug("has before");
            if (!(Boolean) doAction(url, before, req, res, session, chain)) {//若准备失败
                Before annotation = before.getMethod().getAnnotation(Before.class);
                send = annotation.send();//转发到失败页面
                req.setAttribute("notice", annotation.message());//设置提示信息
                doSend(send, req, res, session);
                return;
            }
            log.debug("do before success");
        }//执行Action主体方法
        send = (String) doAction(url, mvcMehod, req, res, session, chain);
        if (after != null) {//若有善后处理方法 执行善后处理方法
            log.debug("has after");
            doAction(url, after, req, res, session, chain);
            log.debug("do after success");
        }
        doSend(send, req, res, session);
    }

    /**
     * 根据指定的url和mvcMmethod执行Action
     * @param url
     * @param mvcMmethod
     * @param request
     * @param response
     * @param session
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    private Object doAction(String url, MVCBean mvcMmethod, HttpServletRequest request, HttpServletResponse response, HttpSession session, FilterChain chain) throws IOException, ServletException {
        if (mvcMmethod == null) {
            chain.doFilter(request, response);
        } else {
            Object[] argsValue = getArgsValue(url,mvcMmethod, request, response, session);
            try {
                return mvcMmethod.getMethod().invoke(mvcMmethod.getController(), argsValue);
            } catch (IllegalAccessException ex) {
                log.error("执行Controller出错！！！！方法不能访问！url:{},mapping:{}",url, mvcMmethod.getMethod().getName(),ex);
            } catch (InvocationTargetException ex) {
                log.error("执行Controller出错！！！！url:{},mapping:{}", url,  mvcMmethod.getMethod().getName(),ex);
            }catch (Exception ex) {
                if(ex instanceof SQLException){
                    IDbManager manager = ConfigCenter.INSTANCE.getManager();
                    try {
                        manager.rollBack();
                    } catch (SQLException e) {
                    }
                    log.error("Exception doAction for url:{} ",url, ex);
                }
                log.error("执行Controller出错！！！！url:{},mapping:{}", url,  mvcMmethod.getMethod().getName(),ex);
            }finally {
                IDbManager manager = ConfigCenter.INSTANCE.getManager();
                try {
                    manager.finalCloseConnection();
                } catch (SQLException ex) {
                    log.error("outterCloseConnection error!", ex);
                }
            }
        }
        return null;
    }

    /**
     * 页面跳转
     * @param send
     * @param request
     * @param response
     * @param session
     * @throws IOException
     */
    private void doSend(String send, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        if (response.isCommitted() || send == null) {
            return;
        }
        if (send.startsWith("redirect:")) {//如果是重定向
            String notice = (String) request.getAttribute("notice");
            if (notice != null && !"".equals(notice.trim())) {//将notice信息放入session访问提示信息丢失
                session.setAttribute("notice", notice);
            }
            response.sendRedirect(send.replaceFirst("redirect:", ""));
            return;
        }
        try {
            request.getRequestDispatcher(send).forward(request, response);
        } catch (ServletException ee) {
            log.error("Hyberbin警告：{}JSP页面有错误！！！！",send, ee);
            response.sendRedirect("");
        }
    }

    /**
     * 为Action中的方法设置参数
     * @param url
     * @param method
     * @param request
     * @param response
     * @param session
     * @return
     */
    private Object[] getArgsValue(String url,MVCBean method, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Class[] argsType = method.getMethod().getParameterTypes();
        if (argsType == null) {
            return null;
        }
        Object[] argsValue = new Object[argsType.length];
        int vindex=0;
        for (int i = 0; i < argsType.length; i++) {
            if (ServletRequest.class.isAssignableFrom(argsType[i])) {
                argsValue[i] = request;
            } else if (ServletResponse.class.isAssignableFrom(argsType[i])) {
                argsValue[i] = response;
            } else if (HttpSession.class.isAssignableFrom(argsType[i])) {
                argsValue[i] = session;
            } else if(method.getVariables().length>0&&vindex<method.getVariables().length){
                argsValue[i]= ConverString.asType(argsType[i],classLoader.getMappingManager().getPathVariable(method,url,vindex),null);
            }else {
                log.error("Controller参数中有不能识别的类型！！！！");
            }
        }
        return argsValue;
    }

    /**
     * 将URL上传来的参数过滤. 有的Tomcat不支持
     * @param request
     */
    private void doGetFilter(HttpServletRequest request) {
        String method = request.getMethod();
        if (method != null && "get".equals(method.trim().toLowerCase())) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap == null || parameterMap.isEmpty()) {
                return;
            }
            Set<String> keySet = parameterMap.keySet();
            for (String key : keySet) {
                String[] gets = parameterMap.get(key);
                if (gets == null || gets.length == 0) {
                    continue;
                }
                for (String item : gets) {
                    item = htmlFilter(item);
                }
            }
        }
    }

    /**
     * 过滤危险的html代码.
     * @param input
     * @return
     */
    private String htmlFilter(String input) {
        if (input == null) {
            return null;
        } else if (input.length() == 0) {
            return input;
        }
        input = input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        input = input.replace("'", "&#39;").replace("\"", "&quot;");
        return input.replace("\n", "<br>");
    }

    public static void setClassLoader(IClassLoader classLoader) {
        MVCFilter.classLoader = classLoader;
    }

    /**
     * 销毁过滤器
     */
    @Override
    public void destroy() {
    }
}
