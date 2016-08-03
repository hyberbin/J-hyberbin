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
package org.jplus.hyb.database.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.IConfigurator;
import org.jplus.util.MD5Util;

/**
 * 单个静态的数据库连接管理.
 * 不是每次操作完成后都关闭数据库连接以提高操作速度.
 * 只有在调用finalCloseConnection才强制关闭连接.
 * 该类中所有方法皆线程安全.
 * @author hyberbin
 */
public class SingleManager extends ADbManager {

    private static final Map<String, Connection> CONN_NAME_MAP = Collections.synchronizedMap(new HashMap<String, Connection>());
    private static final Map<String, Connection> CONN_MD5_MAP = Collections.synchronizedMap(new HashMap<String, Connection>());

    public SingleManager(String defaultConfig) {
        super(defaultConfig);
    }

    @Override
    public synchronized IDbManager newInstance() {
        return super.newInstance();
    }

    @Override
    public synchronized void finalCloseConnection() throws SQLException {
        super.connection.close();
        CONN_NAME_MAP.remove(getDefaultDbConfig().getConfigName());
        CONN_MD5_MAP.remove(MD5Util.MD5(getDefaultDbConfig().getDriver() + getDefaultDbConfig().getUrl() + getDefaultDbConfig().getUsername() + getDefaultDbConfig().getPassword()));
    }

    @Override
    public synchronized void rollBack() throws SQLException {
        super.rollBack();
    }

    @Override
    public synchronized void openTransaction() throws SQLException {
        super.openTransaction();
    }

    @Override
    public synchronized void commit() throws SQLException {
        super.commit();
    }

    @Override
    public synchronized void setConfigurator(IConfigurator configurator) {
        super.setConfigurator(configurator);
    }
    /**
     * 获取数据库连接.
     * 如果两个缓存中关于连接参数的值都为空则新开启一个连接.
     * 若连接参数改变了则关闭原来的连接,新开一个连接替换原来的连接.
     * 如果没有任何改变则返回上次的连接.
     * @param driver 驱动.
     * @param url 连接服务器地址.
     * @param username 用户名.
     * @param password 密码.
     * @return 
     */
    @Override
    protected synchronized Connection getConnection(String driver, String url, String username, String password) {
        String md5 = MD5Util.MD5(driver + url + username + password);
        Connection getByMd5 = CONN_MD5_MAP.get(md5);
        Connection getByName = CONN_NAME_MAP.get(getDefaultDbConfig().getConfigName());
        try {
            if (getByMd5 == null && getByName == null) {
                super.connection = super.getConnection(driver, url, username, password);
                super.connection.close();
                super.connection = super.getConnection(driver, url, username, password);
                CONN_MD5_MAP.put(md5, super.connection);
                CONN_NAME_MAP.put(getDefaultDbConfig().getConfigName(), super.connection);
            } else if (getByMd5 != getByName) {//连接已经更新
                getByName.close();
                super.connection = super.getConnection(driver, url, username, password);
                CONN_MD5_MAP.put(md5, super.connection);
                CONN_NAME_MAP.put(getDefaultDbConfig().getConfigName(), super.connection);
            } else {
                super.connection = CONN_NAME_MAP.get(getDefaultDbConfig().getConfigName());
            }
        } catch (SQLException ex) {
            log.error("获取链接失败!", ex);
        }
        return super.connection;
    }

    @Override
    protected synchronized Connection getConnection(DbConfig config) throws SQLException {
        return super.getConnection(config);
    }

    @Override
    protected synchronized Connection getConnection(String name) throws SQLException {
        return super.getConnection(name);
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        return super.getConnection();
    }

    @Override
    public synchronized DbConfig getDefaultDbConfig() {
        return super.getDefaultDbConfig();
    }
    /**
     * 不真正关闭连接,只提交事务.
     * @throws SQLException 
     */
    @Override
    public synchronized void closeConnection() throws SQLException {
        commit();
    }

}
