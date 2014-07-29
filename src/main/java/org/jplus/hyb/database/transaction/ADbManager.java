/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.transaction;

import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.IConfigurator;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.NullUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author bin
 */
public abstract class ADbManager implements IDbManager {

    protected Logger log = LoggerManager.getLogger(getClass());
    protected Connection connection;

    protected IConfigurator configurator=new SimpleConfigurator();

    @Override
    public Connection getConnection() {
        return connection==null?getConnection(configurator.getDefaultConfig()):connection;
    }

    @Override
    public Connection getConnection(String name) {
        return getConnection(configurator.getDbConfig(name));
    }

    @Override
    public Connection getConnection(DbConfig config) {
        return getConnection(config.getDriver(), config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public Connection getConnection(String driver, String url, String username, String password) {
        NullUtils.validateNull(driver, "driver");
        NullUtils.validateNull(driver, "url");
        NullUtils.validateNull(driver, "username");
        NullUtils.validateNull(driver, "password");
        log.debug("创建数据库连接 driver:{} url:{} username:{} password:{}", driver, url, username, password);
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            log.error("数据库连接错误\t找不到驱动", ex);
        } catch (SQLException ex) {
            log.error("数据库连接错误\t", ex);
        }
        return connection;
    }

    @Override
    public void setConfigurator(IConfigurator configurator) {
        this.configurator = configurator;
    }

}
