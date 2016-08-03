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
package org.jplus.hyb.database.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.IConfigurator;

/**
 *
 * @author Hyberbin
 */
public interface IDbManager {

    public Connection getConnection() throws SQLException;

    public void openTransaction() throws SQLException;

    public void commit() throws SQLException;

    public void rollBack() throws SQLException;

    public void closeConnection() throws SQLException;
    
    public void finalCloseConnection() throws SQLException;

    public void setConfigurator(IConfigurator configurator);
    
    public IDbManager newInstance();
    
    public DbConfig getDefaultDbConfig();
}
