/*
 * Copyright 2014 Hyberbin.
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
