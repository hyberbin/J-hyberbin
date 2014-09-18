/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import org.jplus.hyb.database.config.IConfigurator;

/**
 *
 * @author Hyberbin
 */
public interface IDbManager {

    public Connection getConnection();

    public void openTransaction() throws SQLException;

    public void commit() throws SQLException;

    public void rollBack() throws SQLException;

    public void closeConnection() throws SQLException;
    
    public void finalCloseConnection() throws SQLException;

    public void setConfigurator(IConfigurator configurator);
    
    public IDbManager newInstance();
}
