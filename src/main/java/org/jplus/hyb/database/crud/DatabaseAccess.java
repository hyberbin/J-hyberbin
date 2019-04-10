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
package org.jplus.hyb.database.crud;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.jplus.hyb.database.transaction.IDbManager;

/**
 * 数据库基本操作工具.
 *
 * @author hyberbin
 * @version 2.0
 */
public class DatabaseAccess extends BaseDbTool {

  /**
   * 自带的事务管理器创建数据库操作对象.
   *
   * @param tx 连接.
   */
  public DatabaseAccess(IDbManager tx) {
    super(tx);
  }

  /**
   * 放置一个参数到sql预处理列表.
   *
   * @param parameter 参数.
   */
  public DatabaseAccess setParmeter(Object parameter) {
    log.trace("addParmeter {}", parameter);
    adapter.addParameter(parameter);
    return this;
  }

  /**
   * 清空预处理参数列表.
   */
  public DatabaseAccess clearParmeter() {
    adapter.getParmeters().clear();
    return this;
  }


  /**
   * 数据库更新操作.
   *
   * @param sql SQL语句.
   * @return 受影响的数据条数.
   */
  public int update(String sql) throws SQLException {
    try {
      int update = adapter.update(getConnection(), sql);
      return update;
    } finally {
      tx.closeConnection();
    }
  }

  /**
   * 数据库查询操作.
   *
   * @param sql SQL语句.
   * @return 查询结果.
   */
  public ResultSet query(String sql) throws SQLException {
    try {
      ResultSet findList = adapter.findList(getConnection(), sql);
      return findList;
    } finally {
      tx.closeConnection();
    }
  }

  /**
   * 查询唯一的一条记录.
   *
   * @param sql SQL语句.
   * @return 查询结果.
   */
  public ResultSet querySingle(String sql) throws SQLException {
    try {
      ResultSet findSingle = adapter.findSingle(getConnection(), sql);
      return findSingle;
    } finally {
      tx.closeConnection();
    }
  }

  /**
   * 查询唯一的单值对象.
   *
   * @param sql SQL语句.
   * @return 查询结果.
   */
  public Object queryUnique(String sql) throws SQLException {
    try {
      Object findUnique = adapter.findUnique(getConnection(), sql);
      return findUnique;
    } finally {
      tx.closeConnection();
    }
  }


}
