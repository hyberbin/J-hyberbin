/*
 * Copyright 2015 www.hyberbin.com.
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
package org.jplus.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import org.jplus.hyb.database.bean.FieldColumn;
import org.jplus.hyb.database.util.CacheFactory;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;

/**
 * 对任意类中的成员变量进行set或者get操作
 * @author hyberbin
 */
public class FieldUtil {
    private static final Logger log = LoggerManager.getLogger(FieldUtil.class);
    private static final Class[] EMPTY_ARG=new Class[]{};
    /**
     * 取得一个成员变量的值
     * @param tablebean tablebean
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object tablebean, String fieldName) {
        log.trace("in getFieldValue by fieldName");
        if (tablebean == null || fieldName == null || fieldName.trim().equals("")) {
            return null;
        }
        return Reflections.invokeGetter(tablebean, fieldName);//调用实体类的getXXX方法
    }

    /**
     * 根据注解获取字段值
     * @param tablebean
     * @param fieldAnnotation
     * @return
     */
    public static Object getFieldValue(Object tablebean, Class fieldAnnotation) {
        log.trace("in getFieldValue by fieldAnnotation");
        Field[] declaredFields = tablebean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(fieldAnnotation)) {
                return getFieldValue(tablebean, field.getName());
            }
        }
        return null;
    }

    /**
     * 根据注解获取字段值
     * @param tablebean
     * @param object
     * @param fieldAnnotation
     */
    public static void setFieldValue(Object tablebean, Class fieldAnnotation, Object object) {
        log.trace("in setFieldValue");
        Field[] declaredFields = tablebean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(fieldAnnotation)) {
                setFieldValue(tablebean, field.getName(), object);
            }
        }
    }

    /**
     * 获得本POJO类外键的值
     * @param tablebean 本POJO类
     * @param fieldName 外键字段名称
     * @return
     */
    public static Object getFatherFieldValue(Object tablebean, String fieldName) {
        log.trace("in getFatherFieldValue");
        try {
            Field field = tablebean.getClass().getDeclaredField(fieldName);
            tablebean = getFieldValue(tablebean, fieldName);
            JoinColumn c = field.getAnnotation(JoinColumn.class);//获取实例  
            fieldName = c.name(); //获取元素值
            return getFieldValue(tablebean, fieldName);
        } catch (NoSuchFieldException ex) {
            log.error("获得本POJO类外键的值失败！", ex);
        } catch (SecurityException ex) {
            log.error("未知错误！", ex);
        }
        return null;
    }
    
    /**
     * 获取字段对应的FieldColumn
     * @param field
     * @return
     */
    public static FieldColumn getFieldColumnByCache(Field field) {
        log.trace("in getFieldColumnByCache");
        return CacheFactory.MINSTANCE.getFieldColumn(field.getDeclaringClass(), field.getName());
    }

    /**
     * 获取字段对应的FieldColumn
     * @param field
     * @return
     */
    public static FieldColumn getFieldColumn(Field field) {
        log.trace("in getFieldColumn");
        FieldColumn fieldColumn = new FieldColumn();
        fieldColumn.setField(field);
        field.setAccessible(true);
        if (field.isAnnotationPresent(Column.class)) {
            Column annotation = field.getAnnotation(Column.class);
            fieldColumn.setColumn(annotation.name());
            fieldColumn.setLength(annotation.length());
            fieldColumn.setSqltype(annotation.sqltype());
        } else if (field.isAnnotationPresent(Transient.class)) {
            fieldColumn.setIgnore(true);
        } else if (field.isAnnotationPresent(JoinColumn.class)) {
            JoinColumn annotation = field.getAnnotation(JoinColumn.class);
            fieldColumn.setColumn(annotation.name());
            fieldColumn.setSqltype(annotation.sqltype());
        }
        if (ObjectHelper.isNullOrEmptyString(fieldColumn.getColumn())) {
            fieldColumn.setColumn(field.getName());
        }
        Method getter = Reflections.getAccessibleMethod(field.getDeclaringClass(), Reflections.get(field.getName()), EMPTY_ARG);
        Method setter = Reflections.getAccessibleMethod(field.getDeclaringClass(), Reflections.set(field.getName()), new Class[]{field.getType()});
        fieldColumn.setHasGetterAndSetter(getter!=null&&setter!=null);
        return fieldColumn;
    }

    /**
     * 存入一个实体的成员变量值
     * @param tablebean
     * @param fieldName
     * @param value
     * @return
     */
    public static Object setFieldValue(Object tablebean, String fieldName, Object value) {
        log.trace("in setFieldValue");
        if (value == null) {
            return tablebean;
        }
        Reflections.invokeSetter(tablebean, fieldName, value);
        return tablebean;
    }

    /**
     * 设置本POJO类的外键字段的值
     * @param tablebean 本POJO类
     * @param fieldName 本POJO类字段名
     * @param value 要存的值
     * @return
     */
    public static Object setFatherFieldValue(Object tablebean, String fieldName, Object value) {
        log.trace("in setFatherFieldValue");
        if (value == null) {
            return tablebean;
        }
        Object newInstance = null;
        String newfieldName = null;
        try {
            Field field = tablebean.getClass().getDeclaredField(fieldName);
            JoinColumn c = field.getAnnotation(JoinColumn.class);//获取实例  
            newfieldName = c.name(); //获取元素值  
            newInstance = getFieldValue(tablebean, fieldName);
            if (newInstance == null) {
                newInstance = field.getType().newInstance();
                setFieldValue(tablebean, fieldName, newInstance);
                setFieldValue(newInstance, "primarykey", newfieldName);
            }
        } catch (IllegalAccessException ex) {
            log.error("参数不正确", ex);
        } catch (InstantiationException ex) {
            log.error("创建字段:{}的实例失败！", fieldName,ex);
        } catch (NoSuchFieldException ex) {
            log.error("没有{}字段！",fieldName, ex);
        }
        return setFieldValue(newInstance, newfieldName, value);
    }

    /**
     * 把两个相同类型的对象复制成值也相同
     * @param src 源对象
     * @param dist 目的对象
     */
    public static void clone(Object src, Object dist) {
        log.trace("in clone");
        if (src != null && dist != null && src.getClass().equals(dist.getClass())) {
            Field[] declaredFields = src.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                setFieldValue(dist, field.getName(), getFieldValue(src, field.getName()));
            }
        }
    }

    /**
     * 根据字段名获取Field对象
     * @param clazz
     * @param name
     * @return
     */
    public static Field getField(Class clazz, String name) {
        log.trace("in getField by name");
        return Reflections.getAccessibleField(clazz, name);
    }

    /**
     * 根据注解获取Field对象
     * @param clazz
     * @param annotation
     * @return
     */
    public static Field getField(Class clazz, Class annotation) {
        log.trace("in getField by annotation");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(annotation)) {
                return field;
            }
        }
        return null;
    }
}
