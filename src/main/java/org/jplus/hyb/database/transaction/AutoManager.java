package org.jplus.hyb.database.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动的数据库连接管理器. <br/>
 * 只对指定数据库连接进行管理. <br/>
 * 每次用完连接不关闭，同一个线程中对一个数据库只开启一个连接.
 * @author Hyberbin
 */
public class AutoManager extends ADbManager {

    /** 采用ThreadLocal* */
    protected static final ThreadLocal<Map<String, Connection>> threadLocal = new ThreadLocal<Map<String, Connection>>();

    /**
     * 构造方法.
     * @param defaultConfig 指定数据库配置名.
     */
    public AutoManager(String defaultConfig) {
        super(defaultConfig);
    }

    /**
     * 获取数据连接. 从threadLocal中获取连接.如果连接不能用则新开启连接.
     * @return
     */
    @Override
    public Connection getConnection() {
        Map<String, Connection> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Connection>();
            threadLocal.set(map);
        }
        connection = map.get(defaultConfig);
        if (!validConnection(connection)) {
            log.trace("put connection:{} to threadLocal", defaultConfig);
            connection = super.getConnection();
            map.put(defaultConfig, connection);
            log.debug("reput connection:{} to threadLocal", defaultConfig);
        }
        log.debug("get Connection:{} from threadLocal", defaultConfig);
        return connection;
    }

    /**
     * 验证连接是否可用.
     * @param connection 连接
     * @return
     */
    private boolean validConnection(Connection connection) {
        try {
            return connection != null && connection.isValid(3);
        } catch (SQLException ex) {
            log.error("validConnection:{} error!", ex, defaultConfig);
        }
        return false;
    }

    /**
     * 关闭数据库连接. 在本管理器中没有真正关闭数据库而只是提交事务.
     * @throws SQLException
     */
    @Override
    final public void closeConnection() throws SQLException {
        commit();
        log.trace("use outer manager nothing to do close just commit:{}", defaultConfig);
    }

    /**
     * 最终关闭数据库连接. 用户程序运行到最后或者线程结束的时候释放数据库连接资源.
     * @throws SQLException
     */
    @Override
    public void finalCloseConnection() throws SQLException {
        Map<String, Connection> map = threadLocal.get();
        log.trace("config map isnull:{}", map == null);
        if (map != null) {
            map.remove(defaultConfig);
            log.trace("remove config:{}", defaultConfig);
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        log.trace("in finalCloseConnection:{}", defaultConfig);
    }

    /**
     * 复制一个实例.
     * @return
     */
    @Override
    public IDbManager newInstance() {
        return new AutoManager(defaultConfig);
    }
}
