/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author bin
 */
public abstract class AOuterManager extends ADbManager implements IOuterManager {

    protected final static ThreadLocal<Connection> LOCAL = new ThreadLocal<Connection>();

    @Override
    public Connection getConnection() {
        Connection get = LOCAL.get();
        if (get == null) {
            log.trace("putConnection to ThreadLocal");
            LOCAL.set(super.getConnection());
            log.debug("getConnection from ThreadLocal");
            return LOCAL.get();
        } else {
            log.debug("getConnection from ThreadLocal");
            return get;
        }
    }

    @Override
    final public void commit() throws SQLException {
        log.trace("use outer manager commit nothing");
    }

    @Override
    final public void closeConnection() throws SQLException {
        log.trace("use outer manager nothing to do close");
    }

}
