package org.jplus.hyb.database.transaction;

import java.sql.SQLException;

/**
 * 简单的数据库连接管理器.<br/>
 * 在这个管理器中每次用完连接都自动关闭，会频繁地开启和关闭数据连接.
 * @author Hyberbin
 */
public class SimpleManager extends ADbManager {

    public SimpleManager(String defaultConfig) {
        super(defaultConfig);
    }
    /**
     * 关闭数据连接释放资源.
     * @throws SQLException 
     */
    @Override
    public void closeConnection() throws SQLException {
        commit();
        log.debug("close Connection");
        connection.close();
    }
    /**
     * 复制一个实例.
     * @return 
     */
    @Override
    public IDbManager newInstance() {
        return new SimpleManager(defaultConfig);
    }

}
