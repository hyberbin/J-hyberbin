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
package org.jplus.hyb.database.config;

/**
 * 数据配置接口
 * @author Hyberbin
 */
public class DbConfig {

    public static final String DEFAULT_CONFIG_NAME = "default";
    public static final String DRIVER_SQLITE = "org.sqlite.JDBC";
    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
    public static final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DRIVER = DRIVER_MYSQL;

    public static final String URL_MYSQL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&useOldAliasMetadataBehavior=true&UseOldSyntax=true";
    public static final String URL_ORACLE = "jdbc:oracle:thin:@localhost:1521:ORCL";
    public static final String URL_SQLITE = "jdbc:sqlite:data.db";
    public static final String URL_SQLSERVER = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName";
    
    public static final String USER = "root";
    public static final String PASS = "root";

    public DbConfig(String driver, String url, String username, String password, String configName) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.configName = configName;
    }

    public DbConfig(String url, String username, String password, String configName) {
        this(getDriverFromUrl(url), url, username, password, configName);
    }

    public DbConfig(String url, String configName) {
        this(getDriverFromUrl(url), url, USER, PASS, configName);
    }

    public DbConfig(String configName) {
        this(DRIVER, URL_MYSQL, USER, PASS, configName);
    }

    public DbConfig() {
        this(DEFAULT_CONFIG_NAME);
    }

    /** 驱动 */
    private String driver;
    /** 连接地址 */
    private String url;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 配置名称 */
    private final String configName;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfigName() {
        return configName;
    }

    public static String getMysqlUrl(String ip, String dbname, int port) {
        return "jdbc:mysql://" + ip + ":" + port + "/" + dbname + "?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&useOldAliasMetadataBehavior=true&UseOldSyntax=true";
    }

    public static String getMysqlUrl(String ip, String dbname) {
        return getMysqlUrl(ip, dbname, 3306);
    }
    
    public static String getDriverFromUrl(String url){
        if(url.contains("oracle")){
            return DRIVER_ORACLE;
        }else if(url.contains("mysql")){
            return DRIVER_MYSQL;
        }else if(url.contains("sqlite")){
            return DRIVER_SQLITE;
        }else if(url.contains("sqlserver")){
            return DRIVER_SQLSERVER;
        }else{
            return DRIVER;
        }
    }
}
