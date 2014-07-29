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
        return Reflections.invokeMethod(target, method.getName(), method.getParameterTypes(), args);
    }

}
