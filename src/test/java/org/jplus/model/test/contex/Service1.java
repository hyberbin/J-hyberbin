/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.model.test.contex;

import javax.persistence.Autowired;
import javax.persistence.Resource;
import javax.persistence.Service;

/**
 *
 * @author hyberbin
 */
@Service
public class Service1 {

    @Resource
    private SimpleService1 simpleService1;
    private final SimpleService2 simpleService2;

    @Autowired
    public Service1(SimpleService2 simpleService2) {
        this.simpleService2 = simpleService2;
    }

    public void out() {
        simpleService1.out();
        simpleService2.out();
        System.out.println("Service1.out");
    }

}
