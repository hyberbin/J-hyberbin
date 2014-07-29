/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.util;

import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;

/**
 *
 * @author Hyberbin
 */
public class NullUtils {
     private static final Logger log = LoggerManager.getLogger(NullUtils.class);

    public static boolean isNUll(Object o) {
        return null == o;
    }

    public static void validateNull(Object o) {
        if (isNUll(o)) {
            log.debug("validateNull object is null!");
            throw new IllegalArgumentException();
        }
    }
    public static void validateNull(Object o,String name) {
        if (isNUll(o)) {
            log.debug("validateNull {} is null!",name);
            throw new IllegalArgumentException();
        }
    }

}
