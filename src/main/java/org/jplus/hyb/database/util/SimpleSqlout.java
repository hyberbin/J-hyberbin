/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.util;

import java.sql.SQLOutput;
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
    private Boolean needout;

    @Override
    public void sqlout(String sql, List parmeters) {
        if (isSqlout()) {
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

    @Override
    public void setSqlout(boolean needout) {
        this.needout=needout;
    }

    @Override
    public boolean isSqlout() {
        return needout==null?needout=ConfigCenter.INSTANCE.getConfigurator().sqlOut():needout;
    }

}
