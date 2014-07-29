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
public interface IConfigurator {
    /**
     * 获取默认数据连接
     * @return 
     */
    public DbConfig getDefaultConfig();
    /**
     * 获取指定名称的数据连接
     * @param name 连接名
     * @return 
     */
    public DbConfig getDbConfig(String name);
    /**
     * 是否输出SQL语句
     * @return 
     */
    public boolean sqlOut();
    /**
     * 是否预处理
     * @return 
     */
    public boolean prepare();
}
