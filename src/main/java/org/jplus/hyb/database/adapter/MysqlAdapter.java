/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
