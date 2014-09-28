/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.jplus.hyb.database.util.Pager;

/**
 * 各种数据查询的适配器接口
 * @author Hyberbin
 */
public interface IAdapter {

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
     * @param connection 数据库连接
     * @param sql sql语句
     * @return 查到的结果
     * @throws java.sql.SQLException
     */
    public ResultSet findList(Connection connection,String sql) throws SQLException;

    /**
     * 分页查询
     * @param connection 数据库连接
     * @param sql sql语句
     * @param pager 分页对象
     * @return 查到的结果
     * @throws java.sql.SQLException
     */
    public ResultSet findPageList(Connection connection,String sql,Pager pager) throws SQLException;

    /**
     * 单例查询
     * @param connection 数据库连接
     * @param sql sql语句
     * @return 查到的结果
     * @throws java.sql.SQLException
     */
    public ResultSet findSingle(Connection connection,String sql) throws SQLException;

    /**
     * 单个值查询
     * @param connection 数据库连接
     * @param sql sql语句
     * @return 查到的结果
     * @throws java.sql.SQLException
     */
    public Object findUnique(Connection connection,String sql) throws SQLException;

    /**
     * 更新操作
     * @param connection 数据库连接
     * @param sql sql语句
     * @return 影响的条数
     * @throws SQLException
     */
    public int update(Connection connection,String sql) throws SQLException;

    /**
     * 获取用于区分系统关键字的括号字符
     * @return
     */
    public char[] getQuote();

    /**
     * 输出SQL语句
     */
    public void sqlout();

}
