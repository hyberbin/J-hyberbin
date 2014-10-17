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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.servlet.http.HttpServletRequest;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.ConverString;
import org.jplus.util.FieldUtil;

/**
 * 表单的数据获得
 *
 * @author hyber-bin
 * @version 1.2
 */
public class DataGet {
    private static final Logger log=LoggerManager.getLogger(DataGet.class);
    /**
     * 类型转换，将字符类型转换为字段需要的类型
     *
     * @param field 成员变量
     * @param value 字符串值
     * @return
     */
    public static Object parse(Field field, String value) {
        return value == null ? null : ConverString.asType(field.getType(), value, null);
    }

    /**
     * 根据参数名获得成员变量名
     *
     * @param parameterName 参数名
     * @return
     */
    public static String getFieldName(String parameterName) {
        if (!parameterName.contains(".")) {
            return parameterName;
        } else {
            String rStr = parameterName.substring(parameterName.indexOf('.') + 1);
            if (rStr.contains(".")) {
                log.error("参数名称不合法,只能有一个点！{}",parameterName);
                return null;
            }
            return rStr;
        }
    }

    /**
     * 从request中获取表单数据到pojo类中
     *
     * @param request HttpServletRequest
     * @param formbean POJO类
     * @param parameterName 参数名
     * @param spaceIsNull 空格是否当null
     * @return 
     */
    public static Boolean load(HttpServletRequest request, Object formbean, String parameterName, boolean spaceIsNull) {
        if ("mode".equals(parameterName)) {
            return null;
        }
        try {
            String rValue = request.getParameter(parameterName);
            if (rValue != null && spaceIsNull && "".equals(rValue.trim())) {
                return false;
            }
            parameterName = getFieldName(parameterName);
            Field field;
            try {
                field = formbean.getClass().getDeclaredField(parameterName);
            } catch (NoSuchFieldException noSuchFieldException) {
                return null;
            } catch (SecurityException securityException) {
                return null;
            }
            if (field.isAnnotationPresent(JoinColumn.class)) {
                JoinColumn c = field.getAnnotation(JoinColumn.class);//获取实例 
                Field cfield = field.getType().getDeclaredField(c.name());
                Object value = parse(cfield, rValue);
                FieldUtil.setFatherFieldValue(formbean, parameterName, value);
            } else {
                Object value = parse(field, rValue);
                FieldUtil.setFieldValue(formbean, parameterName, value);
                if (value == null) {
                    return false;
                }
            }

        } catch (NoSuchFieldException ex) {
            log.error("从表单获得参数失败！找不到字段" + parameterName, ex);
        } 
        return null;
    }

    /**
     * 将参数批量存入POJO类
     * @param request HttpServletRequest
     * @param formbean POJO类
     * @param spaceIsNull 空格是否当null
     * @return   */
    public static List<String> loadByParams(HttpServletRequest request, Object formbean, boolean spaceIsNull) {
        Enumeration names = request.getParameterNames();
        List<String> nullList = null;
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Boolean load = load(request, formbean, name, spaceIsNull);
            if (load != null && !load) {
                if (nullList == null) {
                    nullList = new ArrayList<String>();
                }
                nullList.add(name);
            }
        }
        return nullList;
    }

    /**
     * 将参数批量存入多个POJO类
     * @param request HttpServletRequest
     * @param beans POJO类数组
     * @param spaceIsNull 空格是否当null
     */
    public static void loadByParams(HttpServletRequest request, Object[] beans, boolean spaceIsNull) {
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (!name.contains(".")) {
                log.error("参数名称不合法,多个对象时参数没有加点！{}", name);
                continue;
            }
            String beanName = name.substring(0, name.indexOf("."));
            load(request, findBean(beans, beanName), name, spaceIsNull);

        }
    }

    /**
     * 按照POJO类成员变量信息来从表单存入数据
     * @param request HttpServletRequest
     * @param formbean POJO类
     * @param spaceIsNull 空格是否当null
     * @return 
     */
    public static List<String> loadByBean(HttpServletRequest request, Object formbean, boolean spaceIsNull) {
        Field[] fields = formbean.getClass().getDeclaredFields();
        List<String> nullList = null;
        for (Field field : fields) {
            Boolean load = load(request, formbean, field.getName(), spaceIsNull);
            if (load != null && !load) {
                if (nullList == null) {
                    nullList = new ArrayList<String>();
                }
                if (request.getParameterMap().containsKey(field.getName())) {//如果有关于这个字段的就加
                    nullList.add(field.getName());
                }
            }
        }
        return nullList;
    }

    /**
     * 根据名称找到类
     * @param beans POJO类数组
     * @param name 名称
     * @return
     */
    public static Object findBean(Object[] beans, String name) {
        for (Object o : beans) {
            if (o.getClass().getSimpleName().toLowerCase().equals(name)) {
                return o;
            }
        }
        return null;
    }
}
