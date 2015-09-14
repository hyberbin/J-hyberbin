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
package org.jplus.hyb.database.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.jplus.hyb.database.bean.ParmeterPair;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.util.ObjectHelper;

/**
 *
 * @author Hyberbin
 */
public class SimpleSqlout implements ISqlout {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Boolean needout;

    @Override
    public void sqlout(String sql, List<ParmeterPair> parmeters) {
        if (isSqlout()) {
            if (ObjectHelper.isNotEmpty(parmeters)) {
                try {
                    for (ParmeterPair parmeter : parmeters) {
                        Object o = parmeter.getParmeter();
                        if (o != null) {
                            if (o instanceof Number) {
                                sql = sql.replaceFirst("[?]", o + "");
                            } else if (o instanceof Date) {
                                sql = sql.replaceFirst("[?]", "'" + dateFormat.format((Date) o) + "'");
                            } else {
                                sql = sql.replaceFirst("[?]", "'" + o + "'");
                            }
                        } else {
                            sql = sql.replaceFirst("[?]", "null");
                        }
                    }
                } catch (Exception e) {
                }
            }
            System.out.println("sqlout:  " + sql);
        }
    }

    @Override
    public void setSqlout(boolean needout) {
        this.needout = needout;
    }

    @Override
    public boolean isSqlout() {
        return needout == null ? needout = ConfigCenter.INSTANCE.getConfigurator().sqlOut() : needout;
    }

}
