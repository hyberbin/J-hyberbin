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
