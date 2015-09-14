/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.database.bean;

/**
 *
 * @author hyberbin
 */
public class ParmeterPair{
    private final Object parmeter;
    private final FieldColumn fieldColumn;

    public ParmeterPair(Object parmeter, FieldColumn fieldColumn) {
        this.parmeter = parmeter;
        this.fieldColumn = fieldColumn;
    }

    public Object getParmeter() {
        return parmeter;
    }

    public FieldColumn getFieldColumn() {
        return fieldColumn;
    }
    
}
