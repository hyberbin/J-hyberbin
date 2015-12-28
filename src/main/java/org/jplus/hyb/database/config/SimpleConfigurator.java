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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单的配置器.
 * 可以添加多个配置用来在一个项目中连接多个数据库.
 * @author Hyberbin
 */
public final class SimpleConfigurator implements IConfigurator {

    private static final Map<String, DbConfig> DBCONFIGS = Collections.synchronizedMap(new HashMap<String, DbConfig>());
    public static final SimpleConfigurator INSTANCE = new SimpleConfigurator();

    static {//自动添加一个默认的配置
        addConfigurator(new DbConfig());
    }
    private SimpleConfigurator(){
        
    }
    /**
     * 添加一个数据配置.
     * @param config 
     */
    public static void addConfigurator(DbConfig config) {
        DBCONFIGS.put(config.getConfigName(), config);
    }
    /**
     * 获取默认配置
     * @return 
     */
    @Override
    public DbConfig getDefaultConfig() {
        return DBCONFIGS.get(DbConfig.DEFAULT_CONFIG_NAME);
    }
    /**
     * 获取指定名称的数据库配置
     * @param name
     * @return 
     */
    @Override
    public DbConfig getDbConfig(String name) {
        return DBCONFIGS.get(name);
    }
    /**
     * 是否输出数据库
     * @return 
     */
    @Override
    public boolean sqlOut() {
        return true;
    }
    /**
     * 是否预处理
     * @return 
     */
    @Override
    public boolean prepare() {
        return true;
    }
    /**
     * 是否开启事务
     * @return 
     */
    @Override
    public boolean tranceaction() {
        return true;
    }
}
