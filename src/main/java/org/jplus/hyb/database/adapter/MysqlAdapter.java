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
package org.jplus.hyb.database.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jplus.hyb.database.util.Pager;

/**
 * Mysql适配器
 * @author Hyberbin
 */
public class MysqlAdapter extends AAdapter {

    private static final char[] QUOTE = new char[]{'`', '`'};

    

    @Override
    public ResultSet findList(Connection connection,String sql) throws SQLException {
        Statement stm = createStatement(connection, sql);
        if (stm instanceof PreparedStatement) {
            return ((PreparedStatement) stm).executeQuery();
        } else {
            return stm.executeQuery(sql);
        }
    }

    @Override
    public ResultSet findPageList(Connection connection,String sql,Pager pager) throws SQLException {
        return findList(connection, sql+" limit "+pager.getTop()+","+pager.getSize());
    }

    @Override
    public ResultSet findSingle(Connection connection,String sql) throws SQLException {
        return findList(connection, sql+" limit 1");
    }

    @Override
    public Object findUnique(Connection connection,String sql) throws SQLException {
        ResultSet singel = findSingle(connection,sql);
        return singel != null && singel.next() ? singel.getObject(1) : null;
    }

    @Override
    public int update(Connection connection,String sql) throws SQLException {
        Statement stm = createStatement(connection, sql);
        if (stm instanceof PreparedStatement) {
            return ((PreparedStatement) stm).executeUpdate();
        } else {
            return stm.executeUpdate(sql);
        }
    }

    @Override
    public char[] getQuote() {
        return QUOTE;
    }

}
