/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.util.ObjectHelper;

/**
 *
 * @author Hyberbin
 */
public class SimpleSqlout implements ISqlout {
    private final static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void sqlout(String sql, List parmeters) {
        if (ConfigCenter.INSTANCE.getConfigurator().sqlOut()) {
            if (ObjectHelper.isNotEmpty(parmeters)) {
                for (Object o : parmeters) {
                    if(o!=null){
                        if(o instanceof Number){
                            sql = sql.replaceFirst("[?]", o + "");
                        }else if(o instanceof Date){
                            sql = sql.replaceFirst("[?]", "'"+dateFormat.format((Date)o)+"'");
                        }else{
                            sql = sql.replaceFirst("[?]", "'"+o + "'");
                        }
                    }else{
                        sql = sql.replaceFirst("[?]", "null");
                    }
                }
            }
            System.out.println("sqlout:  "+sql);
        }
    }

}
