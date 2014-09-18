/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.config;

/**
 * 数据配置接口
 * @author Hyberbin
 */
public class DbConfig {

    public static final String DEFAULT_CONFIG_NAME = "default";
    private final static String DRIVER = "com.mysql.jdbc.Driver";
    private final static String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&useOldAliasMetadataBehavior=true&UseOldSyntax=true";
    private final static String USER = "root";
    private final static String PASS = "root";


    public DbConfig(String driver, String url, String username, String password, String configName) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.configName = configName;
    }
    
    public DbConfig(String url, String username, String password, String configName) {
        this.driver = DRIVER;
        this.url = url;
        this.username = username;
        this.password = password;
        this.configName = configName;
    }
    
    public DbConfig(String url,String configName) {
        this.driver = DRIVER;
        this.url = url;
        this.username = USER;
        this.password = PASS;
        this.configName = configName;
    }
    
    public DbConfig(String configName) {
        this.driver = DRIVER;
        this.url = URL;
        this.username = USER;
        this.password = PASS;
        this.configName = configName;
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

}
