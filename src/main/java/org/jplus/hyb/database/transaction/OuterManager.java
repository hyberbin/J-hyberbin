/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jplus.hyb.database.transaction;

import java.sql.SQLException;

/**
 *
 * @author bin
 */
public class OuterManager extends AOuterManager{

    @Override
    public void openTransaction() throws SQLException {
        log.debug("open Transaction,setAutoCommit true");
        if(connection!=null)connection.setAutoCommit(true);
    }

    @Override
    public void rollBack() throws SQLException {
        log.debug("transaction rollBack");
        if(connection!=null)connection.rollback();
    }
    

    @Override
    public void submit() throws SQLException {
        log.debug("commit");
        if(connection!=null)connection.commit();
    }

    @Override
    public void close() throws SQLException {
        log.debug("close Connection");
        if(connection!=null)connection.close();
    }
    
}
