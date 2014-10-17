/*
 * Copyright 2014 Hyberbin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Email:hyberbin@qq.com
 */
package org.jplus.hyb.mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;

/**
 * BaseMVCAction.
 * @author Hyberbin
 * @date 2013-6-8 11:44:51
 */
public class BaseMVCAction {
    protected Logger log=LoggerManager.getLogger(getClass());

    /**
     * 获得所有表单数据.
     *
     * @param request 
     * @param formbean
     * @param spaceIsNull 
     * @return 返回为空的字段
     */
    protected List<String> loadByParams(HttpServletRequest request, Object formbean, boolean spaceIsNull) {
        return DataGet.loadByParams(request, formbean, spaceIsNull);
    }

    /**
     * 获得所有表单数据 不建议使用，因为可能不是所有字段都在表单中.
     *
     * @param request 
     * @param formbean
     * @param spaceIsNull 
     * @return 返回为空的字段.
     */
    protected List<String> loadByBean(HttpServletRequest request, Object formbean, boolean spaceIsNull) {
        return DataGet.loadByBean(request, formbean, spaceIsNull);
    }

    /**
     * 获得所有表单数据.
     *
     * @param request 
     * @param formbean POJO类数组.
     * @param spaceIsNull 
     * @return
     */
    protected Object[] loadByParams(HttpServletRequest request, Object[] formbean, boolean spaceIsNull) {
        DataGet.loadByParams(request, formbean, spaceIsNull);
        return formbean;
    }

    /**
     *
     * @param response
     * @param type
     * @param object
     */
    protected void ajax(HttpServletResponse response, String type,Object object) {
        try {
            response.setContentType("text/" + type + ";charset=UTF-8");
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setHeader("pragma", "no-cache");
            response.setHeader("cache-control", "no-cache");
            out.println(object);
            out.flush();
            out.close();
        } catch (IOException ex) {
            log.error("ajax错误！", ex);
        }
    }
  
    /**
     * 过滤doGet提交过来的html字符.
     */
  

    /**
     * 过滤危险的html代码.
     *
     * @param input
     * @return
     */
    protected String htmlFilter(String input) {
        if (input == null) {
            return null;
        } else if (input.length() == 0) {
            return input;
        }
        input = input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        input = input.replace("'", "&#39;").replace("\"", "&quot;");
        return input.replace("\n", "<br>");
    }

    /**
     * 获得客户端IP地址.
     *
     * @param request 
     * @return
     */
    protected String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip.trim())) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip.trim())) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip.trim())) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    /**
     * 设置页面警告信息
     * @param request
     * @param notice 
     */
    protected void setNotice(HttpServletRequest request,String notice){
        request.setAttribute("notice", notice);
    } 
}
