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
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.util.NumberUtils;
import org.jplus.util.ObjectHelper;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jplus.hyb.database.transaction.SingleManager;

/**
 *
 * @author Hyberbin
 */
public class SqliteUtil {

    private static final Logger log = LoggerManager.getLogger(SqliteUtil.class);
    private static final Map<String,String> PropertiesMap=Collections.synchronizedMap(new HashMap<String, String>());

    static {
        SimpleConfigurator.addConfigurator(new DbConfig("org.sqlite.JDBC", "jdbc:sqlite:data.db", "", "", "sqlite"));
        if (!tableExist("properties")) {
            createParmeterTable();
        }
    }
    /**
     * 获取Sqlite的连接管理器.
     * 不提交事务可以加快操作速度.
     * @param commit 是否提交事务.
     * @return 
     */
    public static IDbManager getManager(final boolean... commit) {
        IDbManager manager = new SingleManager("sqlite"){
            @Override
            public synchronized void commit() throws SQLException {
                if(commit.length>0&&!commit[0]){
                    return;
                }else{
                    super.commit(); 
                }
            }
        };
        return manager;
    }
    /**
     * 表是否存在.
     * @param tableName 表名
     * @return 
     */
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
    /**
     * 创建属性表.
     */
    public static void createParmeterTable() {
        String sql = "create table properties(key text,value text);";
        execute(sql);
    }
    /**
     * 执行sql语句.
     * @param sql 
     */
    public static void execute(String sql) {
        DatabaseAccess lite = new DatabaseAccess(getManager());
        try {
            lite.update(sql);
        } catch (SQLException ex) {
            log.error("create table error", ex);
        }
    }
    /**
     * 获取属性.
     * 由于不会对数据库产生数据影响这里都不关闭数据库和提交事务.
     * @param key
     * @return 
     */
    public static String getProperty(String key) {
        String pvalue = PropertiesMap.get(key);
        if(pvalue==null){
            String sql = "select value from properties where key ='" + key + "'";
            DatabaseAccess lite = new DatabaseAccess(getManager(false));
            try {
                Object value = lite.queryUnique(sql);
                pvalue=value == null ? "" : value.toString();
            } catch (SQLException ex) {
                log.error("getProperty error key:{}", ex, key);
            }
        }
        PropertiesMap.put(key,pvalue);
        return pvalue;
    }
    /**
     * 获取对应属性名的布尔值.
     * @param key 属性名.
     * @return 
     */
    public static boolean getBoolProperty(String key) {
        String pvalue =getProperty(key);
        return pvalue == null ? false : "true".equalsIgnoreCase(pvalue);
    }
    /**
     * 获取对应属性名的长整型值.
     * @param key 属性名.
     * @return 
     */
    public static Long getLongProperty(String key) {
        String pvalue =getProperty(key);
        return pvalue == null ? 0 : NumberUtils.parseDouble(pvalue).longValue();
    }
    /**
     * 设置一个属性名和对应的值.
     * 如果缓存中已经有一样的值则不对数据库操作反之则更新数据库.
     * @param key 属性名.
     * @param value 属性对应的值.
     * @param commit 是否提交事务.
     */
    public static void setProperty(String key, String value,boolean... commit ) {
        if (ObjectHelper.isNullOrEmptyString(key) ){
            return ;
        }
        if( ObjectHelper.isNullOrEmptyString(value)) {
            value = "";
        }
        String pvalue =getProperty(key);
        if(value.equals(pvalue)){
            return;
        }
        Properties property = new Properties(key, value);
        Hyberbin<Properties> hyberbin = new Hyberbin(property, getManager(commit));
        try {
            hyberbin.deleteByKey("key");
            hyberbin = new Hyberbin(property, getManager(commit));
            hyberbin.insert("");
            PropertiesMap.put(key,value);
        } catch (SQLException ex) {
            log.error("setProperty key:{},value:{} error!", ex, key, value);
        }
    }
    /**
     * 清空缓存.
     */
    public static void clearProperties() {
        DatabaseAccess databaseAccess = new DatabaseAccess(getManager());
        try {
            databaseAccess.update("delete from Properties");
            PropertiesMap.clear();
        } catch (SQLException ex) {
            log.error("clearProperties error!", ex);
        }
    }
    /**
     * 下面是一些扩展方法,在窗体中如果对象失去焦点则将对象名和值放入缓存.
     * @param field
     * @param name 
     */
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

    public static void bindJCheckBoxField(final JCheckBox field, final String name) {
        field.setSelected(getBoolProperty(name));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                setProperty(name, field.isSelected() + "");
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
            } else if (JCheckBox.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    bindJCheckBoxField((JCheckBox) field.get(frame), field.getName());
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
