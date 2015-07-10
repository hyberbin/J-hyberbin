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

import javax.persistence.Id;
import java.lang.reflect.Field;

/**
 *
 * @author Hyberbin
 */
public class FieldColumn {
    /**成员变量对象*/
    private Field field;
    /**字段名*/
    private String column;
    /**数据长度*/
    private int length;
    /**是否有setter和getter名*/
    private boolean hasGetterAndSetter;
    /**是否被忽略 忽略的时候CRUD操作均不计*/
    private boolean ignore;

    public FieldColumn(Field field, String column, int length, boolean hasGetterAndSetter, boolean ignore) {
        this.field = field;
        this.column = column;
        this.length = length;
        this.hasGetterAndSetter = hasGetterAndSetter;
        this.ignore = ignore;
    }

    public FieldColumn() {
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isHasGetterAndSetter() {
        return hasGetterAndSetter;
    }

    public void setHasGetterAndSetter(boolean hasGetterAndSetter) {
        this.hasGetterAndSetter = hasGetterAndSetter;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }


    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(FieldColumn.class) && column.equals(((FieldColumn) obj).getColumn());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.column != null ? this.column.hashCode() : 0);
        return hash;
    }

    public FieldColumn cloneMe(Field field) {
        return new FieldColumn(field, column, length, hasGetterAndSetter, ignore);
    }

}
