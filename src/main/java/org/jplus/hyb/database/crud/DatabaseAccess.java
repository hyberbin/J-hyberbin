package org.jplus.hyb.database.crud;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.jplus.hyb.database.transaction.IDbManager;

/**
 * 数据库基本操作工具.
 * @version 2.0
 * @author hyberbin
 */
public class DatabaseAccess extends BaseDbTool{
    
    /**
     * 自带的连接创建数据库操作对象.
     * @param tx 连接.
     */
    public DatabaseAccess(IDbManager tx) {
        super(tx);
    }

    /**
     * 放置一个参数到sql预处理列表.
     * @param parameter 参数.
     * @return
     */
    public DatabaseAccess setParmeter(Object parameter) {
        log.trace("addParmeter {}", parameter);
        adapter.addParameter(parameter);
        return this;
    }

    /**
     * 清空预处理参数列表.
     * @return
     */
    public DatabaseAccess clearParmeter() {
        adapter.getParmeters().clear();
        return this;
    }

    

    /**
     * 数据库更新操作.
     * @param sql SQL语句.
     * @return 是否成功.
     * @throws java.sql.SQLException
     */
    public int update(String sql) throws SQLException {
        int update = adapter.update(createStatement(sql), sql);
        tx.closeConnection();
        return update;
    }

    /**
     * 数据库查询操作.
     * @param sql SQL语句.
     * @return 查询结果.
     * @throws java.sql.SQLException
     */
    public ResultSet query(String sql) throws SQLException {
        ResultSet findList = adapter.findList(createStatement(sql), sql);
        tx.closeConnection();
        return findList;
    }

    /**
     * 数据库查询操作.
     * @param sql SQL语句.
     * @return 查询结果.
     * @throws java.sql.SQLException
     */
    public ResultSet querySingle(String sql) throws SQLException {
        ResultSet findSingle = adapter.findSingle(createStatement(sql), sql);
        tx.closeConnection();
        return findSingle;
    }

    /**
     * 数据库查询操作.
     * @param sql SQL语句.
     * @return 查询结果.
     * @throws java.sql.SQLException
     */
    public Object queryUnique(String sql) throws SQLException {
        Object findUnique = adapter.findUnique(createStatement(sql), sql);
        tx.closeConnection();
        return findUnique;
    }

    

}
