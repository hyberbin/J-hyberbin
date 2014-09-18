/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.hyb.database.util.ISqlout;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.ObjectHelper;

/**
 *
 * @author Hyberbin
 */
public abstract class AAdapter implements IAdapter {

    protected final static Logger log = LoggerManager.getLogger(AAdapter.class);
    /** 预处理参数 */
    protected List parmeters = new ArrayList(0);

    @Override
    public Statement createStatement(Connection conn, String sql) throws SQLException {
        Statement stm;
        if (ConfigCenter.INSTANCE.getConfigurator().prepare()) {
            log.trace("createStatement:prepare");
            stm = conn.prepareStatement(sql);
            int index = 1;
            if (ObjectHelper.isNotEmpty(parmeters)) {
                for (Object parmeter : parmeters) {
                    ((PreparedStatement) stm).setObject(index++, parmeter);
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
        parmeters.add(o);
    }

    @Override
    public List getParmeters() {
        log.trace("in getParmeters");
        return parmeters;
    }

    @Override
    public void sqlout(String sql) {
        if (ConfigCenter.INSTANCE.getConfigurator().sqlOut()) {
            ISqlout sqlout = ConfigCenter.INSTANCE.getSqlout();
            if(sqlout!=null){
                sqlout.sqlout(sql, parmeters);
            }else{
                log.error("can't find sqlout adapter,you can set it by ConfigCenter.DEFAULT_INSTANCE.setSqlout(ISqlout sqlout)");
            }
        }
    }

}
