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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.jplus.hyb.database.bean.FieldColumn;
import org.jplus.hyb.database.bean.ParmeterPair;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.hyb.database.util.ISqlout;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.NumberUtils;
import org.jplus.util.ObjectHelper;

/**
 *
 * @author Hyberbin
 */
public abstract class AAdapter implements IAdapter {

    protected static final Logger log = LoggerManager.getLogger(AAdapter.class);
    /** 预处理参数 */
    protected List<ParmeterPair> parmeters = new ArrayList(0);
    protected String sql;

    protected Statement createStatement(Connection conn, String sql) throws SQLException {
        this.sql = sql;
        sqlout();
        Statement stm;
        if (ConfigCenter.INSTANCE.getConfigurator().prepare()) {
            log.trace("createStatement:prepare");
            stm = conn.prepareStatement(sql);
            int index = 1;
            if (ObjectHelper.isNotEmpty(parmeters)) {
                for (ParmeterPair parmeter : parmeters) {
                    FieldColumn fieldColumn = parmeter.getFieldColumn();
                    if(fieldColumn!=null&&fieldColumn.getSqltype()!=-1){
                        ((PreparedStatement) stm).setObject(index++, parmeter.getParmeter(),fieldColumn.getSqltype());
                    }else{
                        ((PreparedStatement) stm).setObject(index++, parmeter.getParmeter());
                    }
                }
            }
        } else {
            log.trace("createStatement:not prepareStatement");
            stm = conn.createStatement();
        }
        return stm;
    }

    @Override
    public void addParameter(Object o) {
        log.trace("addParameter {}", o);
        parmeters.add(new ParmeterPair(o, null));
    }
    
    @Override
    public void addParameter(Object o,FieldColumn fieldColumn) {
        log.trace("addParameter {}", o);
        parmeters.add(new ParmeterPair(o, fieldColumn));
    }

    @Override
    public List getParmeters() {
        log.trace("in getParmeters");
        return parmeters;
    }

    /**
     * 查询总数
     *
     * @param connection 数据库连接
     * @param sql        sql语句
     * @return 查到的结果
     * @throws SQLException
     */
    @Override
    public int getCount(Connection connection, String sql) throws SQLException {
        sql = "select count(*) from (" + sql + ") as count";
        Object findUnique =findUnique(connection, sql);
        return NumberUtils.parseInt(findUnique);
    }

    @Override
    public void sqlout() {
        if (ConfigCenter.INSTANCE.getConfigurator().sqlOut()) {
            ISqlout sqlout = ConfigCenter.INSTANCE.getSqlout();
            if (sqlout != null) {
                sqlout.sqlout(sql, parmeters);
            } else {
                log.error("can't find sqlout adapter,you can set it by ConfigCenter.DEFAULT_INSTANCE.setSqlout(ISqlout sqlout)");
            }
        }
    }
    
}
