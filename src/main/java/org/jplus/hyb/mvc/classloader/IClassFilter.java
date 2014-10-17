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

/**
 * 启动时扫描所有类和JAR包的过滤器.
 * 如果返回true则加载该类，false不加载该类
 * @author hyberbin
 */
public interface IClassFilter {
    /**
     * 根据类来过滤
     * @param clazz 类
     * @return true加载false不加载
     */
    public boolean filterClass(Class clazz);

    /**
     * 根据类的路径来过滤
     * @param path 类的路径
     * @return  true加载false不加载
     */
    public boolean filterClass(String path);

    /**
     * 根据jar包的名称来过滤
     * @param name jar包的名称
     * @return true加载false不加载
     */
    public boolean filterJar(String name);

    /**
     * 加入不加载类的正则表达式
     * @param regx 正则表达式
     */
    public void putExcludeClass(String regx);

    /**
     * 加入不加载jar包的正则表达式
     * @param regx 正则表达式
     */
    public void putExcludeJar(String regx);
}
