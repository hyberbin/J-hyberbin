package org.jplus.hyb.mvc.classloader;

import org.jplus.annotation.Action;
import org.jplus.annotation.After;
import org.jplus.annotation.Before;
import org.jplus.annotation.Mapping;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.hyb.mvc.bean.MVCBean;
import org.jplus.hyb.mvc.mapping.IMappingManager;
import org.jplus.hyb.mvc.mapping.MappingManager;
import org.jplus.util.FileCopyUtils;
import org.jplus.util.Reflections;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 加载所有带有Action注释的类
 *
 * @author Hyberbin
 */
public final class ClassScanner extends ClassLoader implements IClassLoader {

    private final static Logger log = LoggerManager.getLogger(ClassScanner.class);
    private final static String[] separates = new String[]{"/","{","}"};
    private final static Integer[] empty=new Integer[]{};
    /**
     * 类名称列表
     */
    private final List<Class> classList = new ArrayList();
    /**
     * 本类唯一实例
     */
    public static ClassScanner MY_INSTANCE = new ClassScanner();
    private IClassFilter classFilter = ClassFilter.INSTANCE;
    private IMappingManager mappingManager=new MappingManager();

    /**
     * 私有构造方法不允许外部创建.
     */
    private ClassScanner() {
        loadClassPath();
        loadJar();
        setActionMap();
    }

    /**
     * 用递归方法搜索类.
     *
     * @param path 类路径
     */
    private void getClassList(String path) {
        if (path == null) {
            path = ClassScanner.class.getResource("/").getPath();
        }
        log.info("getClassList path:{}", path);
        File file = new File(path);
        File[] listFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    if (classFilter.filterClass(file.getName())) {
                        log.trace("getClass {}", file.getName());
                        try {
                            byte[] byts = FileCopyUtils.copyToByteArray(file);
                            Class defineClass = defineClass(null, byts, 0, byts.length);
                            Class clazz = Class.forName(defineClass.getName());//如果不调用这个不能初始化
                            if (classFilter.filterClass(clazz)) {
                                log.debug("load class from class:{}", clazz.getName());
                                classList.add(clazz);
                            }
                            return false;
                        } catch (ClassNotFoundException ex) {
                            log.trace("load class error,fileName:{}", file.getName());
                        } catch (LinkageError ex) {
                            log.trace("load class error,fileName:{}", file.getName());
                        } catch (IOException ex) {
                            log.trace("IOException load class error,fileName:{}", file.getName());
                        }
                    }
                }
                return false;
            }
        });
        if (listFiles != null) {
            for (File searchedFile : listFiles) {
                getClassList(searchedFile.getPath());
            }
        }
    }


    /**
     * 设置Action和Urlr的映射表.
     */
    private int setActionMap() {
        int count = 0;
        for (Class clazz : classList) {
            try {
                if (clazz.isAnnotationPresent(Action.class)) {
                    Object mvcObject = clazz.newInstance();
                    Action annotation = (Action) clazz.getAnnotation(Action.class);
                    String[] urlPatterns = annotation.urlPatterns();
                    for (String url : urlPatterns) {
                        for (Method method : Reflections.getAllMethods(clazz)) {
                            MVCBean mvcb;
                            if (method.isAnnotationPresent(Mapping.class)) {
                                Mapping annotation1 = method.getAnnotation(Mapping.class);
                                String mode = annotation1.name().toLowerCase();
                                Set<Integer> ints=new TreeSet<Integer>();
                                if(mode.contains(separates[1])){
                                    String[] split = (url+mode).split(separates[0]);
                                    for (int i = 0; i < split.length; i++) {
                                        String s = split[i];
                                        if(s.startsWith(separates[1])&&s.endsWith(separates[2])){
                                            ints.add(i);
                                        }
                                    }
                                }
                                Integer[] aints=ints.toArray(new Integer[]{});
                                mvcb = new MVCBean(method, mvcObject,aints);
                                if (annotation1.isDefault()) {
                                    mappingManager.putMapping(url, mvcb);
                                }else{
                                    mappingManager.putMapping(url+mode, mvcb);
                                }
                                mappingManager.putMapping(url, mvcb);
                            } else if (method.isAnnotationPresent(After.class)) {
                                mvcb = new MVCBean(method, mvcObject,empty);
                                mappingManager.putAfter(url, mvcb);
                            } else if (method.isAnnotationPresent(Before.class)) {
                                mvcb = new MVCBean(method, mvcObject,empty);
                               mappingManager.putBefore(url, mvcb);
                            } else {
                                continue;
                            }
                            count++;
                        }
                    }
                }
            } catch (IllegalAccessException ex) {
                log.error("GetAllClass不能访问类：{}", clazz.getName(), ex);
            } catch (InstantiationException ex) {
                log.error("GetAllClass不能创建实例：{}", clazz.getName(), ex);
            }
        }
        log.info("---------------加载了{}个Class，{}个Url----------------", classList.size(), count);
        return count;
    }

    /**
     * 通过Url获得MVC主体模型实例.
     *
     * @param url URL
     * @return
     */
    @Override
    public MVCBean getActionClass(String url) {
        return mappingManager.getMapping(url);
    }

    /**
     * 通过Url获得MVC准备模型实例.
     *
     * @param url
     * @return
     */
    @Override
    public MVCBean getActionBefore(String url) {
        return mappingManager.getBefore(url);
    }

    /**
     * 通过Url获得MVC善后模型实例.
     *
     * @param url
     * @return
     */
    @Override
    public MVCBean getActionAfter(String url) {
        return mappingManager.getAfter(url);
    }

    /**
     * 重新加载一次
     */
    public void reload() {
        MY_INSTANCE = new ClassScanner();
    }

    public void setClassFilter(IClassFilter classFilter) {
        this.classFilter = classFilter;
    }

    public void setMappingManager(IMappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    @Override
    public IMappingManager getMappingManager() {
        return mappingManager;
    }

    /***
     * 加载classes下面的散装类
     */
    @Override
    public void loadClassPath() {
        getClassList(null);
    }
    /**
     * 加载项目所有引用的jar包
     */
    @Override
    public void loadJar() {
        String[] libs = System.getProperty("java.class.path").split(";");
        for (String lib : libs) {
            if (classFilter.filterJar(lib)) {
                log.info("loadJar jar{}", lib);
                try {
                    final JarFile jar = new JarFile(lib);
                    Enumeration entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        final JarEntry entry = (JarEntry) entries.nextElement();
                        InputStream input = jar.getInputStream(entry);
                        try {
                            byte[] byts = FileCopyUtils.copyToByteArray(input);
                            if (classFilter.filterClass(entry.getName())) {
                                Class defineClass = defineClass(null, byts, 0, byts.length);
                                Class clazz = Class.forName(defineClass.getName());//如果不调用这个不能初始化
                                if (classFilter.filterClass(clazz)) {
                                    log.debug("load class from jar class{}", defineClass.getName());
                                    classList.add(clazz);
                                }
                            }
                        } catch (ClassNotFoundException ex) {
                            log.trace("load jar class error,entryName:{}", entry.getName());
                        } catch (LinkageError ex) {
                            log.trace("load jar class error,entryName:{}", entry.getName());
                        } catch (IOException ex) {
                            log.trace("IOException load jar class error,entryName:{}", entry.getName());
                        }
                    }
                } catch (IOException ex) {
                    log.trace("IOException load jar class error,jar Name:{}", lib, ex);
                }
            }
        }
    }

}
