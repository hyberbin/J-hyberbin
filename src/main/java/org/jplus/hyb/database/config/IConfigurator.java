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

/**
 * 配置文件读取器接口.
 * 本接口的实现类都应该以单例的形式存在.
 * @author Hyberbin
 */
public interface IConfigurator {
    /**
     * 获取默认数据连接.
     * @return 
     */
    public DbConfig getDefaultConfig();
    /**
     * 获取指定名称的数据连接.
     * @param name 连接名
     * @return 
     */
    public DbConfig getDbConfig(String name);
    /**
     * 是否输出SQL语句.
     * @return 
     */
    public boolean sqlOut();
    /**
     * 是否预处理.
     * @return 
     */
    public boolean prepare();
    
    /**
     * 是否启用事务.
     * @return 
     */
    public boolean tranceaction();
}
