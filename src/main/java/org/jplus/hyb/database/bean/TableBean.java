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
package org.jplus.hyb.database.bean;

import org.jplus.util.ObjectHelper;

import java.util.List;
import java.util.Map;
import org.jplus.util.IgnoreCaseMap;

/**
 *
 * @author Hyberbin
 */
public class TableBean {

    private String tableName;
    private List<FieldColumn> columns;
    private Map<String, FieldColumn> columnMap;
    private String primaryKey="id";

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<FieldColumn> getColumns() {
        return columns;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setColumns(List<FieldColumn> columns) {
        this.columns = columns;
        this.columnMap = new IgnoreCaseMap<String, FieldColumn>();
        if (ObjectHelper.isNotEmpty(columns)) {
            for (FieldColumn column : columns) {
                columnMap.put(column.getColumn(), column);
            }
        }
    }

    public Map<String, FieldColumn> getColumnMap() {
        return columnMap;
    }
    
    
}
