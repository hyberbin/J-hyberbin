/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jplus.util;

import java.math.BigDecimal;

/**
 *
 * @author Hyberbin
 */
public class NumberUtils {
    /**
     * 格式化数字
     * @param num  数字
     * @param scale 保留几位小数
     * @return
     */
    public static Float format(Object num, int scale) {
        BigDecimal b = new BigDecimal(num.toString());
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static int parseInt(Object o) {
        if (o == null) {
            return 0;
        }else if(o instanceof Integer){
           return (Integer)o;
        }
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Double parseDouble(Object o){
        if (o == null) {
            return 0d;
        }else if(o instanceof Double){
            return (Double)o;
        }
        try {
            return Double.parseDouble(o.toString());
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    public static Integer[] parseInts(String str){
        String[] split = str.split(",");
        Integer[] integers=new Integer[split.length];
        for(int i=0;i<split.length;i++){
            integers[i]=parseInt(split[i]);
        }
        return integers;
    }
}

