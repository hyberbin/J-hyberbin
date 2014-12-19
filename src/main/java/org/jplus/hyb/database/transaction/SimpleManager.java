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
 * 简单的数据库连接管理器.<br/>
 * 在这个管理器中每次用完连接都自动关闭，会频繁地开启和关闭数据连接.
 * @author Hyberbin
 */
public class SimpleManager extends ADbManager {

    public SimpleManager(String defaultConfig) {
        super(defaultConfig);
    }
    /**
     * 关闭数据连接释放资源.
     * @throws SQLException 
     */
    @Override
    public void closeConnection() throws SQLException {
        commit();
        log.debug("close Connection");
        connection.close();
    }

}
