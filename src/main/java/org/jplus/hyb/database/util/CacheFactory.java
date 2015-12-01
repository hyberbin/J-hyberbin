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
package org.jplus.hyb.database.util;

import org.jplus.hyb.database.bean.FieldColumn;
import org.jplus.hyb.database.bean.TableBean;
import org.jplus.hyb.log.LocalLogger;
import org.jplus.hyb.log.Logger;
import org.jplus.util.FieldUtil;
import org.jplus.util.ObjectHelper;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jplus.util.Reflections;

/**
 * 缓存工厂
 * @author hyberbin
 */
public class CacheFactory {

    private static final Logger log = new LocalLogger();//LoggerManager.getLogger(CacheFactory.class);//此处不能用代理日志

    /** 此对象唯一的实例 */
    public static CacheFactory MINSTANCE = new CacheFactory();
    /** 所有Po信息映射集合 */
    private final HashMap<Class, TableBean> hyberbinMap = new HashMap<Class, TableBean>();
    /** *所有的方法映射集合
     * 在参数个数为0或者1的时候String是方法名+参数个数，其它时候String是方法名+各参数的类型名 */
    private final Map<Class, Map<String, Method>> methodMap = new HashMap<Class, Map<String, Method>>();
    /** 所有字段映射集合* */
    private final Map<Class, Map<String, Field>> fieldMap = new HashMap<Class, Map<String, Field>>();

    /**
     * 私有构造方法不允许其它类实例化
     */
    private CacheFactory() {
    }


    /**
     * 存放一个PO类信息到hyberbinMap
     * @param po PO类
     * @param tableBean 字段信息
     */
    private void putHyberbin(Class po, TableBean tableBean) {
        log.trace("in putHyberbin");
        hyberbinMap.put(po, tableBean);
        log.debug("存放一个PO类信息到hyberbinMap，po:{}", po.getName());
    }

    /**
     * 返回PO类字段信息
     * @param po PO类
     * @return
     */
    public TableBean getHyberbin(Class po) {
        log.trace("in getHyberbin");
        TableBean tableBean = hyberbinMap.get(po);
        if (tableBean == null) {
            tableBean = new TableBean();
            if (po.isAnnotationPresent(Table.class)) {
                Table annotation = (Table) po.getAnnotation(Table.class);
                tableBean.setTableName(annotation.name());
            } else {
                tableBean.setTableName(po.getSimpleName());
            }
            Field[] declaredFields = Reflections.getAllFields(po).toArray(new Field[]{});
            List<FieldColumn> columns = new ArrayList<FieldColumn>(0);
            for (Field field : declaredFields) {
                columns.add(FieldUtil.getFieldColumn(field));
                if(field.isAnnotationPresent(Id.class)){
                    tableBean.setPrimaryKey(field.getName());
                }
            }
            tableBean.setColumns(columns);
            putHyberbin(po, tableBean);
        }
        log.trace("out getHyberbin");
        return tableBean;
    }

    /**
     * 获得封装的字段信息
     * @param po PO类
     * @param fieldName
     * @return
     */
    public FieldColumn getFieldColumn(Class po, String fieldName) {
        log.trace("in getFieldColumn");
        TableBean hyberbin = getHyberbin(po);
        return hyberbin.getColumnMap().get(fieldName);
    }


    /**
     * 将方法缓存到缓冲区
     * @param o 对象
     * @param types 参数列表
     * @param method 方法对象
     */
    public void putMethod(Object o, Class<?>[] types, Method method) {
        log.trace("in putMethod");
        Class clazz = o instanceof Class ? ((Class) o) : o.getClass();
        Map<String, Method> objectMethodMap = methodMap.get(clazz);
        if (objectMethodMap == null) {
            objectMethodMap = new HashMap<String, Method>();
            methodMap.put(clazz, objectMethodMap);
        }
        String key =method.getName()+getTypes(types);
        objectMethodMap.put(key, method);
        log.debug("方法缓存加入：{}，key：{}，参数个数:{}", clazz.getName(), key,types.length);
    }

    private String getTypes(Class<?>[] types) {
        if (ObjectHelper.isEmpty(types)) {
            return "";
        }
        StringBuilder type = new StringBuilder();
        for (Class clazz : types) {
            type.append(",").append(clazz.getName());
        }
        return type.toString();
    }

    /**
     * 获取方法
     * @param o 对象
     * @param name 方法名
     * @param types 参数列表
     * @return
     */
    public Method getMethod(Object o, String name, Class<?>[] types) {
        Class clazz = o instanceof Class ? ((Class) o) : o.getClass();
        log.trace("in getMethod from {}, get:{}", clazz.getName(), name);
        Map<String, Method> objectMethodMap = methodMap.get(clazz);
        if (objectMethodMap != null) {
            return objectMethodMap.get(name+getTypes(types));
        }
        log.debug("缓存中没有类：{}，方法名：{},参数个数：{}", clazz.getName(), name, types.length);
        return null;
    }

    /**
     * 向缓存中存入字段信息
     * @param o 对象
     * @param field 字段
     */
    public void putField(Object o, Field field) {
        Class clazz = o instanceof Class ? ((Class) o) : o.getClass();
        log.trace("in putField:put class {},fieldName {}", clazz.getName(), field.getName());
        Map<String, Field> fields = fieldMap.get(clazz);
        if (fields == null) {
            fields = new HashMap<String, Field>();
            fieldMap.put(clazz, fields);
        }
        fields.put(field.getName(), field);
        log.trace("out putField");
    }

    /**
     * 从缓存中获取对象的字段
     * @param o 对象
     * @param name 字段名
     * @return
     */
    public Field getField(Object o, String name) {
        Class clazz = o instanceof Class ? ((Class) o) : o.getClass();
        log.trace("in getField:get class {},fieldName {}", clazz.getName(), name);
        Map<String, Field> fields = fieldMap.get(clazz);
        if (fields != null) {
            return fields.get(name);
        }
        log.debug("缓存中没有对象：{},字段：{}的信息", clazz.getName(), name);
        return null;
    }
}
