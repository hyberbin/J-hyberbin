/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.log;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.jplus.hyb.database.crud.Hyberbin;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hyberbin
 */
public class Test {
    
    public static void main(String[] args) throws SQLException {
        LocalLogger.setLevel(LocalLogger.TRACE);
        LoggerManager.setLogFactory(LoggerFactory.class);
        Hyberbin hyberbin = new Hyberbin();
        List<Map> mapList = hyberbin.getMapList("select * from a");
        System.out.println(mapList);
    }
    
    public static void testDefault() throws SQLException{
        LocalLogger.setLevel(LocalLogger.TRACE);
        LoggerManager.setLogFactory(LoggerFactory.class);
        Hyberbin hyberbin = new Hyberbin();
        List<Map> mapList = hyberbin.getMapList("select * from a");
        System.out.println(mapList);
    }
}
