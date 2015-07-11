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
package org.jplus.hyb.database.sqlite;

import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.crud.DatabaseAccess;
import org.jplus.hyb.database.crud.Hyberbin;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.database.transaction.SimpleManager;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.NumberUtils;
import org.jplus.util.ObjectHelper;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.lang.reflect.Field;
import java.sql.SQLException;

/**
 *
 * @author Hyberbin
 */
public class SqliteUtil {

    private static final Logger log = LoggerManager.getLogger(SqliteUtil.class);

    static {
        if (!tableExist("properties")) {
            createParmeterTable();
        }
    }

    public static IDbManager getManager() {
        IDbManager manager = new SimpleManager("sqlite");
        SimpleConfigurator.addConfigurator(new DbConfig("org.sqlite.JDBC", "jdbc:sqlite:data.db", "", "", "sqlite"));
        return manager;
    }

    public static boolean tableExist(String tableName) {
        DatabaseAccess lite = new DatabaseAccess(getManager());
        try {
            Object count = lite.queryUnique("SELECT COUNT(*) FROM sqlite_master where type='table' and name='" + tableName + "'");
            return Integer.valueOf(1).equals(count);
        } catch (SQLException ex) {
            log.error("check table exist error tablename:{}", ex, tableName);
        }
        return false;
    }

    public static void createParmeterTable() {
        String sql = "create table properties(key text,value text);";
        createTable(sql);
    }
    
    public static void createTable(String sql) {
        DatabaseAccess lite = new DatabaseAccess(getManager());
        try {
            lite.update(sql);
        } catch (SQLException ex) {
            log.error("create table error", ex);
        }
    }

    public static String getProperty(String key) {
        String sql = "select value from properties where key ='" + key + "'";
        DatabaseAccess lite = new DatabaseAccess(getManager());
        try {
            Object value = lite.queryUnique(sql);
            return value == null ? null : value.toString();
        } catch (SQLException ex) {
            log.error("getProperty error key:{}", ex, key);
        }
        return null;
    }

    public static boolean getBoolProperty(String key) {

        String sql = "select value from properties where key ='" + key + "'";
        DatabaseAccess lite = new DatabaseAccess(getManager());
        try {
            Object value = lite.queryUnique(sql);
            return value == null ? false : "true".equalsIgnoreCase(value.toString());
        } catch (SQLException ex) {
            log.error("getProperty error key:{}", ex, key);
        }
        return false;
    }

    public static Long getLongProperty(String key) {
        String sql = "select value from properties where key ='" + key + "'";
        DatabaseAccess lite = new DatabaseAccess(getManager());
        try {
            Object value = lite.queryUnique(sql);
            return value == null ? 0 : NumberUtils.parseDouble(value).longValue();
        } catch (SQLException ex) {
            log.error("getProperty error key:{}", ex, key);
        }
        return 0l;
    }

    public static void setProperty(String key, String value) {
        if (ObjectHelper.isNullOrEmptyString(key) ){
            return ;
        }else if( ObjectHelper.isNullOrEmptyString(value)) {
            value = "";
        }
        Properties property = new Properties(key, value);
        Hyberbin<Properties> hyberbin = new Hyberbin(property, getManager());
        try {
            hyberbin.deleteByKey("key");
            hyberbin = new Hyberbin(property, getManager());
            hyberbin.insert("");
        } catch (SQLException ex) {
            log.error("setProperty key:{},value:{} error!", ex, key, value);
        }
    }

    public static void clearProperties() {
        DatabaseAccess databaseAccess = new DatabaseAccess(getManager());
        try {
            databaseAccess.update("delete from Properties");
        } catch (SQLException ex) {
            log.error("clearProperties error!", ex);
        }
    }

    public static void bindJTextField(final JTextField field, final String name) {
        field.setText(getProperty(name));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                setProperty(name, field.getText());
            }
        });
        if (field instanceof JPasswordField) {
            field.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    ((JPasswordField) field).setEchoChar((char) 0);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    ((JPasswordField) field).setEchoChar('*');
                }
            });
        }
    }

    public static void bindJRadioField(final JRadioButton field, final String name) {
        field.setSelected(getBoolProperty(name));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                setProperty(name, !field.isSelected() + "");
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (!field.isSelected()) {
                    setProperty(name, "true");
                }
            }
        });
    }

    public static void bindJComboxField(final JComboBox field, final String name) {
        field.setSelectedItem(getProperty(name));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                setProperty(name, field.getSelectedItem().toString());
            }
        });
    }

    public static void bindAllField(JFrame frame) {
        Field[] fields = frame.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (JTextField.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    bindJTextField((JTextField) field.get(frame), field.getName());
                } catch (Exception ex) {
                    log.error("绑定事件错误", ex);
                }
            } else if (JRadioButton.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    bindJRadioField((JRadioButton) field.get(frame), field.getName());
                } catch (Exception ex) {
                    log.error("绑定事件错误", ex);
                }
            } else if (JComboBox.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    bindJComboxField((JComboBox) field.get(frame), field.getName());
                } catch (Exception ex) {
                    log.error("绑定事件错误", ex);
                }
            }
        }

    }
}

class Properties {

    private String key;
    private String value;

    public Properties() {
    }

    public Properties(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
