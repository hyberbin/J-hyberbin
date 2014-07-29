/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.mvc.classloader;

import org.jplus.annotation.Action;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author bin
 */
public class ClassFilter implements IClassFilter {

    private static final Set<String> classExcludes = new HashSet<String>();
    private static final Set<String> jarExcludes = new HashSet<String>();
    public static final ClassFilter INSTANCE = new ClassFilter();

    private ClassFilter() {
    }
    /**
     * 根据类的路径来过滤
     * @param path 类的路径
     * @return  true加载false不加载
     */
    @Override
    public boolean filterClass(String path) {
        if (path.contains("$") || !path.endsWith(".class")) {
            return false;
        }
        boolean contains = classExcludes.contains(path);
        if (!contains) {
            for (String ex : classExcludes) {
                if (path.matches(ex)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
    /**
     * 根据jar包的名称来过滤
     * @param name jar包的名称
     * @return true加载false不加载
     */
    @Override
    public boolean filterJar(String name) {
        if (name.toLowerCase().endsWith(".jar")) {
            name = name.substring(name.lastIndexOf(File.separator) + 1);
            boolean contains = classExcludes.contains(name);
            if (!contains) {
                for (String ex : classExcludes) {
                    if (name.matches(ex)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
        return false;
    }
    /**
     * 根据类来过滤
     * @param clazz 类
     * @return true加载false不加载
     */
    @Override
    public boolean filterClass(Class clazz) {
        return !clazz.isInterface() && !clazz.isAnnotation() && clazz.isAnnotationPresent(Action.class);
    }
    /**
     * 加入不加载类的正则表达式
     * @param regx 正则表达式
     */
    public void putExcludeClass(String regx) {
        classExcludes.add(regx);
    }
    /**
     * 加入不加载jar包的正则表达式
     * @param regx 正则表达式
     */
    public void putExcludeJar(String regx) {
        jarExcludes.add(regx);
    }

}
