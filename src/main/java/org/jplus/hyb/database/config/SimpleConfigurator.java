/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.config;

/**
 *
 * @author Hyberbin
 */
public class SimpleConfigurator implements IConfigurator {

    private final static String DRIVER = "com.mysql.jdbc.Driver";
    private final static String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&useOldAliasMetadataBehavior=true&UseOldSyntax=true";
    private final static String USER = "root";
    private final static String PASS = "root";

    private String driver = DRIVER;
    private String url = URL;
    private String user = USER;
    private String pass = PASS;

    public SimpleConfigurator(String driver, String url, String user, String pass) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public SimpleConfigurator() {
    }

    public SimpleConfigurator(String url, String user, String pass) {
        this.driver = DRIVER;
        this.url = url;
        this.user = user;
        this.pass = pass;
    }
    
    public SimpleConfigurator(String url) {
        this.driver = DRIVER;
        this.url = url;
        this.user = USER;
        this.pass = PASS;
    }

    @Override
    public DbConfig getDefaultConfig() {
        return new DbConfig(driver, url, user, pass, DbConfig.DEFAULT_CONFIG_NAME);
    }

    @Override
    public DbConfig getDbConfig(String name) {
        return new DbConfig(driver, url.replaceFirst("test", name), user, pass, name);
    }

    @Override
    public boolean sqlOut() {
        return true;
    }

    @Override
    public boolean prepare() {
        return true;
    }

}
