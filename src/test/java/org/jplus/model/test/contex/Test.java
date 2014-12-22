/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.model.test.contex;

import org.jplus.hyb.mvc.classloader.ClassScanner;
import org.jplus.hyb.mvc.contex.ObjectContex;

/**
 *
 * @author hyberbin
 */
public class Test {

    public static void main(String[] args) {
        ClassScanner scanner = ClassScanner.MY_INSTANCE;
        Service2 resource = ObjectContex.CONTEX.getResource("Service2");
        resource.out();
    }
}
