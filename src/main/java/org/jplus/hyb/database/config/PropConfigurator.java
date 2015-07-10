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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.LoadProperties;
import org.jplus.util.NullUtils;

/**
 * 从properties文件中读取数据相关配置文件. <br/>
 * 例如： driver=com.mysql.jdbc.Driver.<br/>
 * url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&useOldAliasMetadataBehavior=true&UseOldSyntax=true.<br/>
 * user=root.<br/>
 * pass=root.<br/>
 * sqlout=true.<br/>
 * prepare=true.<br/>
 * tranceaction=true.<br/>
 * 如果有多个数据则在driver,url,user,pass前加上配置名称.<br/>
 * 如：name-driver,name-url...
 * @author Hyberbin
 */
public class PropConfigurator implements IConfigurator {

    private static final Logger log = LoggerManager.getLogger(PropConfigurator.class);
    private final LoadProperties dbProperties;
    private static final Map<String, DbConfig> configMap = new HashMap<String, DbConfig>();
    private Boolean sqlout;
    private Boolean prepare;
    private Boolean tranceaction;

    public PropConfigurator(String proptiesPath) {
        dbProperties = new LoadProperties(proptiesPath);
    }

    public PropConfigurator(InputStream inputStream) {
        dbProperties = new LoadProperties(inputStream);
    }

    public PropConfigurator() throws IOException {
        this(PropConfigurator.class.getClassLoader().getResource("database.properties").openStream());
    }

    /**
     * 获取默认的数据连接
     * @return
     */
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

    /**
     * 获取指定名称的数据连接
     * @param name
     * @return
     */
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

    /**
     * 是否输出SQL语句
     * @return
     */
    @Override
    public boolean sqlOut() {
        log.trace("in sqlOut");
        if (sqlout == null) {
            sqlout = dbProperties.getBoolProperties("sqlout");
        }
        log.debug("sql out is :{}", sqlout);
        return sqlout;
    }

    /**
     * 是否使用预处理
     * @return
     */
    @Override
    public boolean prepare() {
        log.trace("in prepare");
        if (prepare == null) {
            prepare = dbProperties.getBoolProperties("prepare");
        }
        log.debug("prepare is :{}", prepare);
        return prepare;
    }

    @Override
    public boolean tranceaction() {
        log.trace("in tranceaction");
        if (tranceaction == null) {
            tranceaction = dbProperties.getBoolProperties("tranceaction");
        }
        log.debug("tranceaction is :{}", tranceaction);
        return tranceaction;
    }

}
