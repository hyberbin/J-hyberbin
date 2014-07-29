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

    public DbConfig(String configName) {
        this.configName = configName;
    }

    public DbConfig(String driver, String url, String username, String password, String configName) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.configName = configName;
    }

    public DbConfig() {
        this.configName = DEFAULT_CONFIG_NAME;
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
