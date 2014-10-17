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
package org.jplus.hyb.database.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成SQL语句
 *
 * @version 1.2
 * @author hyberbin
 */
public class GetSql {

    private final List fields;
    private final List values;
    private final List conditions;

    /**
     * 构造方法
     */
    public GetSql() {
        fields = new ArrayList();
        values = new ArrayList();
        conditions = new ArrayList();
    }

    /**
     * 添加一个字段
     * @param field
     * @param value
     */
    public void add(String field, String value) {
        fields.add(field);
        values.add(value);
        conditions.add("");
    }

    /**
     *
     * @param field
     * @param value
     * @param condition
     */
    public void add(String field, String value, String condition) {
        fields.add(field);
        values.add(value);
        if (condition == null) {
            conditions.add("");
        } else if (condition.equals("where")) {
            conditions.add("where");
        } //如果为null，表示值为空时不update该值
        else if (condition.equals("null")) {
            conditions.add("null");
        } else {
            conditions.add("");
        }
    }

    /**
     * 得到insert语句
     *
     * @param from
     * @return 插入语句
     */
    public String getInsert(String from) {
        StringBuilder insertString = new StringBuilder();
        StringBuffer fieldsString = new StringBuffer();
        StringBuffer valuesString = new StringBuffer();
        for (int i = 0; i < fields.size(); i++) {
            if (values.get(i) != null && !values.get(i).equals("")) {
                fieldsString.append(fields.get(i)).append(",");
                valuesString.append(values.get(i)).append(",");
            }
        }

        if (fieldsString.length() > 0) {
            fieldsString.deleteCharAt(fieldsString.length() - 1).insert(0, "(").append(")");
        }
        if (valuesString.length() > 0) {
            valuesString.deleteCharAt(valuesString.length() - 1).insert(0, "(").append(")");
        }

        insertString.append("insert into ").append(from).append(fieldsString).append(" values").append(valuesString);
        return insertString.toString();
    }

    /**
     * 得到update语句
     *
     * @param from
     * @return 更新语句
     */
    public String getUpdate(String from) {
        StringBuilder updateString = new StringBuilder();
        StringBuffer valuesString = new StringBuffer();
        StringBuffer conditionsString = new StringBuffer();
        String field, value, quote;
        for (int i = 0; i < fields.size(); i++) {
            field = (String) fields.get(i);
            value = (String) values.get(i);
            if ("".equals(value)) {
                value = null;
            }
            if (conditions.get(i).equals("")) {
                valuesString.append(field).append("=").append(value).append(",");
            } else if (conditions.get(i).equals("null")) {
                if (value != null) {
                    valuesString.append(field).append("=").append(value).append(",");
                }
            } else {
                conditionsString.append(" and ").append(field).append(" = ").append(value);
            }
        }

        if (valuesString.length() > 0) {
            valuesString.deleteCharAt(valuesString.length() - 1);
        }
        if (conditionsString.length() > 0) {
            conditionsString.delete(0, 4);
        }

        updateString.append("update ").append(from).append(" set ").append(valuesString).append(" where").append(conditionsString);
        return updateString.toString();
    }

    /**
     *
     * @param selectString
     * @return 查询语句
     */
    public String getQuery(String selectString) {
        return getQuery(selectString, "");
    }

    /**
     * 得到查询语句
     *
     * @param selectString select子句
     * @param orderby orderby子句
     * @return 查询语句
     */
    public String getQuery(String selectString, String orderby) {
        StringBuilder queryString = new StringBuilder();
        StringBuffer conditionsString = new StringBuffer();
        String field, value, quote;
        for (int i = 0; i < fields.size(); i++) {
            field = (String) fields.get(i);
            value = (String) values.get(i);
            if (value != null && !value.equals("")) {
                conditionsString.append(" and ").append(field).append(" = ").append(value);
            }
        }
        if (selectString.indexOf("where") == -1) {
            conditionsString.delete(0, 5).insert(0, "where");
        }
        queryString.append(selectString).append(" ").append(conditionsString).append("").append(orderby);
        return queryString.toString();
    }

    /**
     * 得到sqlserver的分面显示语句
     *
     * @param tableName 表名
     * @param strWhere Where
     * @param key 关键词
     * @param strOrder
     * @param pageSize 页尺寸
     * @param top 从第几条开始
     * @return sqlserver的分面显示语句
     */
    public String sqlserverPageSql(String tableName, String strWhere, String key, String strOrder, int pageSize, int top) {
        String strSQL;
        String strTmp;
        if (strOrder.contains("desc")) {
            strTmp = "<(select min";
            strOrder = " order by " + key + " desc";
        } else {
            strTmp = ">(select max";
            strOrder = " order by " + key + " asc";
        }

        //如果是第一页就执行以上代码，这样会加快执行速度
        if (top == 0) {
            if (!strWhere.equals("")) {
                strSQL = "select top " + pageSize + " * from " + tableName + " " + strWhere + " " + strOrder;
            } else {
                strSQL = "select top " + pageSize + " * from " + tableName + " " + strOrder;
            }
        } //以下代码赋予了strSQL以真正执行的SQL代码
        else {
            strSQL = "select top " + pageSize + " * from " + tableName + " where " +  key + " " + strTmp + "(" + key + ") from (select top " +top + " " + key + " from " + tableName + strOrder + ") as tblTmp)" + strOrder;

            if (!strWhere.equals("")) {
                strSQL = "select top " + pageSize + " * from " + tableName + " where " +  key + " " + strTmp + "(" + key + ") from (select top " + top + " " + key + " from " + tableName + " " + strWhere + " " + strOrder + ") as tblTmp) and " + strWhere.replaceFirst("where", "") + " " + strOrder;
            }
        }
        return strSQL;
    }
}