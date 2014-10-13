/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.transaction;

import java.sql.SQLException;

/**
 *
 * @author Hyberbin
 */
public class TxManager extends AutoManager{

    public TxManager(String defaultConfig) {
        super(defaultConfig);
    }
    
    /**
     * 关闭数据库连接. 在本管理器中没有真正关闭数据库而只是提交事务.
     * @throws SQLException
     */
    @Override
    public void closeConnection() throws SQLException {
        log.trace("use outer manager nothing to do close ");
    }

    /**
     * 最终关闭数据库连接. 用户程序运行到最后或者线程结束的时候释放数据库连接资源.
     * @throws SQLException
     */
    @Override
    public void finalCloseConnection() throws SQLException {
        getConnection();
        commit();
        super.finalCloseConnection();
    }
    
    /**
     * 复制一个实例.
     * @return
     */
    @Override
    public IDbManager newInstance() {
        return new TxManager(defaultConfig);
    }
}
