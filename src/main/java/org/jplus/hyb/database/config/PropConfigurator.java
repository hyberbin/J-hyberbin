/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.LoadProperties;
import org.jplus.util.NullUtils;

/**
 *
 * @author Hyberbin
 */
public class PropConfigurator implements IConfigurator {

    private final static Logger log = LoggerManager.getLogger(PropConfigurator.class);
    private LoadProperties dbProperties = null;
    private final static Map<String, DbConfig> configMap = new HashMap<String, DbConfig>();
    private Boolean sqlout;
    private Boolean prepare;

    public PropConfigurator(String dbProp, String fieldProp, String typeProp) {
        dbProperties = new LoadProperties(dbProp);
    }

    public PropConfigurator() {
        try {
            dbProperties = new LoadProperties(PropConfigurator.class.getClassLoader().getResource("database.properties").openStream());
        } catch (IOException ex) {
            log.error("获取默认配置文件失败", ex);
        }
    }

    @Override
    public DbConfig getDefaultConfig() {
        log.trace("in getDefaultConfig");
        NullUtils.validateNull(dbProperties, "配置文件");
        DbConfig defaultDbConfig = configMap.get(DbConfig.DEFAULT_CONFIG_NAME);
        if (defaultDbConfig == null) {
            defaultDbConfig = new DbConfig(dbProperties.getProperty("driver"), dbProperties.getProperty("url"), dbProperties.getProperty("user"), dbProperties.getProperty("pass"), DbConfig.DEFAULT_CONFIG_NAME);
            configMap.put(DbConfig.DEFAULT_CONFIG_NAME, defaultDbConfig);
        }
        return defaultDbConfig;
    }

    @Override
    public DbConfig getDbConfig(String name) {
        log.trace("in DbConfig");
        NullUtils.validateNull(name, "配置:" + name);
        NullUtils.validateNull(dbProperties, "配置文件");
        DbConfig DbConfig = configMap.get(name);
        if (DbConfig == null) {
            DbConfig = new DbConfig(dbProperties.getProperty(name + "-driver"), dbProperties.getProperty(name + "-url"), dbProperties.getProperty(name + "-user"), dbProperties.getProperty(name + "-pass"), name);
            configMap.put(name, DbConfig);
        }
        return DbConfig;
    }

    @Override
    public boolean sqlOut() {
        log.trace("in sqlOut");
        if (sqlout == null) {
            sqlout = dbProperties.getBoolProperties("sqlout");
        }
        log.debug("sql out is :{}", sqlout);
        return sqlout;
    }

    @Override
    public boolean prepare() {
        log.trace("in prepare");
        if (prepare == null) {
            prepare = dbProperties.getBoolProperties("prepare");
        }
        log.debug("prepare is :{}", prepare);
        return prepare;
    }
}
