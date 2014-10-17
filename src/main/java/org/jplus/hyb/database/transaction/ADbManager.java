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
package org.jplus.hyb.database.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.IConfigurator;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.NullUtils;

/**
 * 数据库连接管理器.
 * @author hyberbin
 */
public abstract class ADbManager implements IDbManager {

    protected Logger log = LoggerManager.getLogger(getClass());
    protected Connection connection;
    protected String defaultConfig = DbConfig.DEFAULT_CONFIG_NAME;
    protected IConfigurator configurator = SimpleConfigurator.INSTANCE;
    /**
     * 数据库名称.
     * @param defaultConfig 
     */
    public ADbManager(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }
    /**
     * 获取默认数据连接.
     * @return 
     */
    @Override
    public Connection getConnection() {
        return getConnection(defaultConfig);
    }
    /**
     * 获取指定数据库连接.
     * @param name
     * @return 
     */
    protected Connection getConnection(String name) {
        return getConnection(configurator.getDbConfig(name));
    }
    /**
     * 获取指定数据库配置的连接.
     * @param config
     * @return 
     */
    protected Connection getConnection(DbConfig config) {
        return getConnection(config.getDriver(), config.getUrl(), config.getUsername(), config.getPassword());
    }
    /**
     * 用相关参数获取数据库连接.
     * @param driver 驱动
     * @param url 地址
     * @param username 用户名
     * @param password 密码
     * @return 
     */
    protected Connection getConnection(String driver, String url, String username, String password) {
        NullUtils.validateNull(driver, "driver");
        NullUtils.validateNull(driver, "url");
        NullUtils.validateNull(driver, "username");
        NullUtils.validateNull(driver, "password");
        log.debug("创建数据库连接 driver:{} url:{} username:{} password:{}", driver, url, username, password);
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            openTransaction();
        } catch (ClassNotFoundException ex) {
            log.error("数据库连接错误\t找不到驱动", ex);
        } catch (SQLException ex) {
            log.error("数据库连接错误\t", ex);
        }
        return connection;
    }
    /**
     * 设置数据库的配置管理器.
     * @param configurator 
     */
    @Override
    public void setConfigurator(IConfigurator configurator) {
        this.configurator = configurator;
    }
    /**
     * 提交事务.
     * @throws SQLException 
     */
    @Override
    public void commit() throws SQLException {
        if (connection != null&&configurator.tranceaction()) {
            connection.commit();
            log.debug("commit");
        }
    }
    /**
     * 开启事务.
     * @throws SQLException 
     */
    @Override
    public void openTransaction() throws SQLException {
        if (!connection.isClosed()&&configurator.tranceaction()&&connection.getAutoCommit()) {
            log.debug("open Transaction,setAutoCommit false");
            connection.setAutoCommit(false);
        }
    }
    /**
     * 回滚事务.
     * @throws SQLException 
     */
    @Override
    public void rollBack() throws SQLException {
        if (!connection.isClosed()&&configurator.tranceaction()) {
            log.debug("transaction rollback");
            connection.rollback();
        }
    }
    /**
     * 最终关闭数据库连接.
     * 用于整个项目只用一个连接或者一个线程用一个连接的情况.
     * @throws SQLException 
     */
    @Override
    public void finalCloseConnection() throws SQLException {
        log.debug("finalCloseConnection do nothing!");
    }

}
