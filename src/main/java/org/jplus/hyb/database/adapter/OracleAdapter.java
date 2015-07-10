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
package org.jplus.hyb.database.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jplus.hyb.database.util.Pager;

/**
 * Oracle适配器
 * @author Hyberbin
 */
public class OracleAdapter extends AAdapter {
    private static final char [] QUOTE=new char[]{'"','"'};

    @Override
    public char [] getQuote() {
        return QUOTE ;
    }

    @Override
    public ResultSet findList(Connection connection, String sql) throws SQLException {
        Statement statement = createStatement(connection, sql);
        if (statement instanceof PreparedStatement) {
            return ((PreparedStatement) statement).executeQuery();
        } else {
            return statement.executeQuery(sql);
        }
    }

    @Override
    public ResultSet findPageList(Connection connection, String sql, Pager pager) throws SQLException {
        sql = "select * from ( select row_.*, rownum rownum_ from ( "+sql+") row_ where rownum <= "+(pager.getTop()+pager.getSize())+") where rownum_ > "+pager.getTop();
        return findList(connection, sql);
    }

    @Override
    public ResultSet findSingle(Connection connection, String sql) throws SQLException {
        return findList(connection, "select res.*,rownum from (" + sql + ") res where rownum=0");
    }

    @Override
    public Object findUnique(Connection connection, String sql) throws SQLException {
         ResultSet singel = findSingle(connection, sql);
        return singel!=null&&singel.next()?singel.getObject(1):null;
    }

    @Override
    public int update(Connection connection, String sql) throws SQLException {
        Statement statement = createStatement(connection, sql);
        if (statement instanceof PreparedStatement) {
            return ((PreparedStatement) statement).executeUpdate();
        } else {
            return statement.executeUpdate(sql);
        }
    }

}
