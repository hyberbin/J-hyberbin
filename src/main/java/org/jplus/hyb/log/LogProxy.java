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
package org.jplus.hyb.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.jplus.util.Reflections;

/**
 *
 * @author Hyberbin
 */
public class LogProxy implements InvocationHandler {

    private Object target;
    private static final LocalLogger loglogger=new LocalLogger();

    /**
     * 绑定委托对象并返回一个代理类
     * @param target
     * @return
     */
    public Object bind(Object target) {
        this.target = target;
        //取得代理对象  
        return Proxy.newProxyInstance(LocalLogger.class.getClassLoader(),
                LocalLogger.class.getInterfaces(), this);
    }

    @Override
    /**
     * 调用方法
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Method accessibleMethod = Reflections.getAccessibleMethod(target, method.getName(), method.getParameterTypes());
        if(accessibleMethod==null){
            return Reflections.invokeMethod(loglogger, method.getName(), method.getParameterTypes(), args);
        }
        return Reflections.invokeMethod(target, method.getName(), method.getParameterTypes(), args);
    }

}
