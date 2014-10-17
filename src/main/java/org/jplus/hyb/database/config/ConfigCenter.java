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
package org.jplus.hyb.database.config;

import org.jplus.hyb.database.adapter.IAdapter;
import org.jplus.hyb.database.adapter.MysqlAdapter;
import org.jplus.hyb.database.crud.DatabaseAccess;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.database.transaction.SimpleManager;
import org.jplus.hyb.database.util.ISqlout;
import org.jplus.hyb.database.util.SimpleSqlout;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.Reflections;

/**
 * 本框架的配置中心.
 *
 * @version 1.5
 * @author hyber-bin
 */
public class ConfigCenter {

    private static final Logger log = LoggerManager.getLogger(ConfigCenter.class);
    public static ConfigCenter INSTANCE = new ConfigCenter();
    
    /** 配置文件读取器 */
    private IConfigurator configurator;
    /** 事务管理器* */
    private IDbManager manager;
    /** sql语句输出器 */
    private String sqlout;
    /** 数据操作适配器 */
    private String adapter;

    /**
     * 私有构造方法禁止外部创建
     */
    private ConfigCenter() {
        configurator = SimpleConfigurator.INSTANCE;
        log.info("use configurator:{}", configurator.getClass().getName());
        manager = new SimpleManager(DbConfig.DEFAULT_CONFIG_NAME);
        log.info("use database manager:{}", manager);
        adapter = MysqlAdapter.class.getName();
        log.info("use database adapter:{}", adapter);
        sqlout = SimpleSqlout.class.getName();
        log.info("use sqlout adapter:{}", sqlout);
    }

    public void setConfigurator(IConfigurator configurator) {
        this.configurator = configurator;
        log.info("use configurator:{}", configurator.getClass().getName());
    }

    public IConfigurator getConfigurator() {
        return configurator;
    }

    public void setManager(String manager,String configName) {
        this.manager = (IDbManager) Reflections.instance(manager,new Class[]{String.class},new Object[]{configName});
        log.info("use database manager:{},defaultConfigName", manager,configName);
    }

    public void setManager(IDbManager manager) {
        this.manager = manager;
        log.info("use database manager:{}", manager);
    }

    public IDbManager getManager() {
        IDbManager manager = this.manager.newInstance();
        manager.setConfigurator(getConfigurator());
        return manager;
    }

    public IAdapter getDefaultAdapter() {
        return (IAdapter) Reflections.instance(adapter);
    }

    /**
     * 获取一个数据库操作对象.
     *
     * @return
     */
    public DatabaseAccess getDatabase() {
        return new DatabaseAccess(getManager());
    }
    private ISqlout sqloutInstance;
    public ISqlout getSqlout() {
        if(sqloutInstance==null){
            sqloutInstance=(ISqlout) Reflections.instance(sqlout);
        }
        return sqloutInstance;
    }

    public void setAdapter(Class<? extends IAdapter> adapter) {
        this.adapter = adapter.getName();
        log.trace("setAdapter {}", this.adapter);
    }

    public void setAdapter(String adapter) {
        this.adapter = adapter;
        log.trace("setAdapter {}", this.adapter);
    }

    public void setSqlout(ISqlout sqlout) {
        this.sqlout = sqlout.getClass().getName();
        log.trace("setSqlout {}", sqlout.getClass().getName());
    }

    public void setSqlout(String sqlout) {
        this.sqlout = sqlout;
        log.trace("setSqlout {}", sqlout);
    }

}
