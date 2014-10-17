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
