/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.IConfigurator;

/**
 *
 * @author Hyberbin
 */
public interface IDbManager {

    public Connection getConnection();

    public void openTransaction() throws SQLException;

    public void commit() throws SQLException;

    public void rollBack() throws SQLException;

    public void closeConnection() throws SQLException;

    /**
     * 获取指定名称的数据库连接
     * @param name
     * @return
     */
    public Connection getConnection(String name);

    public Connection getConnection(DbConfig config);

    /**
     * 根据指定条件获取数据连接
     * @param driver 驱动
     * @param url 地址
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public Connection getConnection(String driver, String url, String username, String password);

    public void setConfigurator(IConfigurator configurator);
}
