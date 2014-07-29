package org.jplus.hyb.mvc.bean;

import java.lang.reflect.Method;

/**
 * MVCBean说明.
 * @author Hyberbin
 * @date 2013-6-8 11:50:48
 */
public class MVCBean {

    private Method method;
    private Object controller;
    private Integer[] variables;

    public MVCBean(Method method, Object controller, Integer[] variables) {
        this.method = method;
        this.controller = controller;
        this.variables = variables;
    }

    public Method getMethod() {
        return method;
    }

    public Object getController() {
        return controller;
    }

    public Integer[] getVariables() {
        return variables;
    }


}
