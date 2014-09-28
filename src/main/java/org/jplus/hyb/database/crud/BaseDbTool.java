/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.crud;

import java.sql.Connection;
import org.jplus.hyb.database.adapter.IAdapter;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;

/**
 *
 * @author Hyberbin
 */
public abstract class BaseDbTool {

    protected final Logger log = LoggerManager.getLogger(getClass());
    /** 数据适配器 默认是mysql适配器 */
    protected IAdapter adapter = ConfigCenter.INSTANCE.getDefaultAdapter();
    /** 数据库连接对象 */
    protected IDbManager tx;

    protected BaseDbTool(IDbManager tx) {
        this.tx = tx;
    }

    /**
     * 获取连接对象.
     * @return 连接对象.
     */
    protected Connection getConnection() {
        return tx.getConnection();
    }

    public IAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(IAdapter adapter) {
        this.adapter = adapter;
    }
}
