/**
 * 基本日志管理类 此日志对象是一个接口 通过配置注射进来
 */
package org.jplus.hyb.log;

import java.util.logging.Level;
import org.jplus.util.Reflections;

/**
 * 日志管理类 .
 * <P>
 * 要使用其他日志请在database.properties文件中配置类名.</p>
 * <p>
 * DatabaseINI会在系统加载的时候读取日志类.</p>
 * <p>
 * 没有配置默认加载LocalLogger.</p>
 * @author hyberbin
 */
public class LoggerManager {

    private LoggerManager() {

    }

    /**
     * 日志对象,默认为本地日志
     */
    private final static Logger logger = new LocalLogger();
    private static Object logFactory = null;
    private static final Class[] ARG_TYPE = new Class[]{String.class};
    /**
     * 注入日志工厂对象
     * @param logFactory
     */
    public static void setLogFactory(Object logFactory) {
        LoggerManager.logFactory = logFactory;
    }

    public static Object getLogFactory() {
        return logFactory;
    }

    /**
     * 设置日志类
     * @param className 类名
     */
    public static void setLogFactory(String className) {
        setLogFactory(LoggerManager.class.getClassLoader(), className);
    }

    /**
     * 设置日志类
     * @param classLoader
     * @param className 类名
     */
    public static void setLogFactory(ClassLoader classLoader, String className) {
        try {
            setLogFactory(classLoader.loadClass(className));
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger("LoggerManage设置日志类失败！找不到类").log(Level.SEVERE, null, ex);
        }
    }

    public static Logger getDefaultLogger() {
        return getLogger("global");
    }

    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        return logFactory==null?logger:(Logger) new LogProxy().bind(Reflections.invokeMethod(logFactory, "getLogger", ARG_TYPE, new Object[]{name}));
    }

}
