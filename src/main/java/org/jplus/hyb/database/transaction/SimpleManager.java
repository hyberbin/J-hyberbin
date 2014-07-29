/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.transaction;

import java.sql.SQLException;

/**
 *
 * @author Hyberbin
 */
public class SimpleManager extends ADbManager {

    @Override
    public void openTransaction() throws SQLException {
        log.debug("open Transaction,setAutoCommit true");
        connection.setAutoCommit(true);
    }

    @Override
    public void commit() throws SQLException {
        log.debug("commit");
        connection.commit();
    }

    @Override
    public void rollBack() throws SQLException {
        log.debug("transaction rollback");
        connection.rollback();
    }

    @Override
    public void closeConnection() throws SQLException {
        log.debug("close Connection");
        connection.close();
    }

}
