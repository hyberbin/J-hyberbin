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
package org.jplus.hyb.database.crud;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jplus.hyb.database.adapter.OracleAdapter;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.database.transaction.SimpleManager;
import org.jplus.model.User;
import org.junit.Test;

/**
 * Created by hyberbin on 15/9/14.
 */
public class TestOracel {
    static {
        SimpleConfigurator.addConfigurator(new DbConfig(DbConfig.DRIVER_ORACLE, "jdbc:oracle:thin:@192.168.1.138:1521:base", "platform", "platform", "oracle"));
    }

    public static IDbManager getOracleManager() {
        return new SimpleManager("oracle");
    }

    @Test
    public void testGetData(){
        Hyberbin<User> hyberbin=new Hyberbin<User>(new User(),getOracleManager());
        try {
            List<User> showAll = hyberbin.showAll();
            System.out.println(showAll.size());
        } catch (SQLException ex) {
            Logger.getLogger(TestOracel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void testInsert(User user){
        Hyberbin<User> hyberbin=new Hyberbin<User>(user,getOracleManager());
        try {
            hyberbin.insert("");
        } catch (SQLException ex) {
            Logger.getLogger(TestOracel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void testInsert(){
        Hyberbin<User> hyberbin=new Hyberbin<User>(new User(),getOracleManager());
        try {
            List<User> showAll = hyberbin.showAll();
            System.out.println(showAll.size());
            User get = showAll.get(0);
            get.setId("21");
            get.setVersion(8888);
            testInsert(get);
        } catch (SQLException ex) {
            Logger.getLogger(TestOracel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void testDelete(){
        User user=new User();
        user.setId("21");
        Hyberbin<User> hyberbin=new Hyberbin<User>(user,getOracleManager());
        try {
            int delete = hyberbin.delete(" where trim(\"ID\")='21'");
            System.out.println(delete);
        } catch (SQLException ex) {
            Logger.getLogger(TestOracel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
