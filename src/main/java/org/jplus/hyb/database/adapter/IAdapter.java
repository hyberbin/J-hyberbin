/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.jplus.hyb.database.util.Pager;

/**
 * 各种数据查询的适配器接口
 * @author Hyberbin
 */
public interface IAdapter {

    /**
     * 创建数据操作对象
     * @param conn 数据连接
     * @param sql sql
     * @return
     * @throws SQLException
     */
    public Statement createStatement(Connection conn,String sql) throws SQLException;

    /**
     * 添加预处理参数
     * @param o
     */
    public void addParameter(Object o);

    /**
     * 获得预处理参数
     * @return
     */
    public List getParmeters();

    /**
     * 查询简单集合
     * @param statement
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet findList(Statement statement, String sql) throws SQLException;

    /**
     * 分页查询
     * @param statement
     * @param pager 分页对象
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet findPageList(Statement statement, Pager pager, String sql) throws SQLException;

    /**
     * 单例查询
     * @param statement
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet findSingle(Statement statement, String sql) throws SQLException;

    /**
     * 单个值查询
     * @param statement
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public Object findUnique(Statement statement, String sql) throws SQLException;

    /**
     * 更新操作
     * @param statement
     * @param sql
     * @return
     * @throws SQLException
     */
    public int update(Statement statement, String sql) throws SQLException;

    /**
     * 获取用于区分系统关键字的括号字符
     * @return
     */
    public char[] getQuote();
    /**
     * 输出SQL语句
     * @param sql
     */
    public void sqlout(String sql);

}
