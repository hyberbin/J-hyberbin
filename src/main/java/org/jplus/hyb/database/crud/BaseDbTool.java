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
package org.jplus.hyb.database.crud;

import java.sql.Connection;
import org.jplus.hyb.database.adapter.IAdapter;
import org.jplus.hyb.database.adapter.MysqlAdapter;
import org.jplus.hyb.database.adapter.OracleAdapter;
import org.jplus.hyb.database.adapter.SqlserverAdapter;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;

/**
 *
 * @author Hyberbin
 */
public abstract class BaseDbTool {

    protected final Logger log = LoggerManager.getLogger(getClass());
    /** 数据适配器 默认是mysql适配器 */
    protected IAdapter adapter;
    /** 数据库连接对象 */
    protected IDbManager tx;

    protected BaseDbTool(IDbManager tx) {
        this.tx = tx;
        String driver = tx.getDefaultDbConfig().getDriver();
        if(driver.contains("oracle")){
            adapter=new OracleAdapter();
        }else if(driver.contains("mysql")){
            adapter=new MysqlAdapter();
        }else if(driver.contains("sqlite")){
            adapter=new MysqlAdapter();
        }else if(driver.contains("sqlserver")){
            adapter=new SqlserverAdapter();
        }else{
            adapter= ConfigCenter.INSTANCE.getDefaultAdapter();
        }
    }

    /**
     * 获取连接对象.
     * @return 连接对象.
     */
    protected Connection getConnection() {
        return tx.getConnection();
    }

    public IAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(IAdapter adapter) {
        this.adapter = adapter;
    }
}
