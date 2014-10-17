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
package org.jplus.hyb.database.transaction;

import java.sql.SQLException;

/**
 *
 * @author Hyberbin
 */
public class TxManager extends AutoManager{

    public TxManager(String defaultConfig) {
        super(defaultConfig);
    }
    
    /**
     * 关闭数据库连接. 在本管理器中没有真正关闭数据库而只是提交事务.
     * @throws SQLException
     */
    @Override
    public void closeConnection() throws SQLException {
        log.trace("use outer manager nothing to do close ");
    }

    /**
     * 最终关闭数据库连接. 用户程序运行到最后或者线程结束的时候释放数据库连接资源.
     * @throws SQLException
     */
    @Override
    public void finalCloseConnection() throws SQLException {
        getConnection();
        commit();
        super.finalCloseConnection();
    }
    
    /**
     * 复制一个实例.
     * @return
     */
    @Override
    public IDbManager newInstance() {
        return new TxManager(defaultConfig);
    }
}
