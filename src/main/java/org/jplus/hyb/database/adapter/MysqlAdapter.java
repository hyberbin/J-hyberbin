/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.adapter;

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

    private final static char[] QUOTE = new char[]{'`', '`'};

    @Override
    public ResultSet findList(Statement statement, String sql) throws SQLException {
        sqlout(sql);
        if (statement instanceof PreparedStatement) {
            return ((PreparedStatement) statement).executeQuery();
        } else {
            return statement.executeQuery(sql);
        }
    }

    @Override
    public ResultSet findPageList(Statement statement, Pager pager, String sql) throws SQLException {
        return findList(statement, sql + " limit " + pager.getTop() + "," + pager.getSize());
    }

    @Override
    public ResultSet findSingle(Statement statement, String sql) throws SQLException {
        return findList(statement, sql + " limit 1");
    }

    @Override
    public Object findUnique(Statement statement, String sql) throws SQLException {
        ResultSet singel = findSingle(statement, sql);
        return singel != null && singel.next() ? singel.getObject(1) : null;
    }

    @Override
    public int update(Statement statement, String sql) throws SQLException {
        sqlout(sql);
        if (statement instanceof PreparedStatement) {
            return ((PreparedStatement) statement).executeUpdate();
        } else {
            return statement.executeUpdate(sql);
        }
    }

    @Override
    public char[] getQuote() {
        return QUOTE;
    }

}
