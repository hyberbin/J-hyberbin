/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.mvc.classloader;

import org.jplus.hyb.mvc.bean.MVCBean;
import org.jplus.hyb.mvc.mapping.IMappingManager;

/**
 *
 * @author bin
 */
public interface IClassLoader {
    /***
     * 加载classes下面的散装类
     */
    public void loadClassPath();

    /**
     * 加载项目所有引用的jar包
     */
    public void loadJar();
    
    
    /**
     * 通过Url获得MVC善后模型实例.
     *
     * @param url
     * @return
     */
    public MVCBean getActionAfter(String url);

    /**
     * 通过Url获得MVC准备模型实例.
     *
     * @param url
     * @return
     */
    public MVCBean getActionBefore(String url);

    /**
     * 通过Url获得MVC主体模型实例.
     *
     * @param url URL
     * @return
     */
    public MVCBean getActionClass(String url);

    public IMappingManager getMappingManager() ;
}
