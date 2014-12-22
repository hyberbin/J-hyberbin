/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.model.test.contex;

import javax.persistence.Resource;
import javax.persistence.Service;

/**
 *
 * @author hyberbin
 */
@Service
public class Service2 {

    @Resource
    private SimpleService1 simpleService1;
    @Resource
    private Service1 service1;
    @Resource
    private SimpleService2 simpleService2;

    public void out() {
        simpleService1.out();
        service1.out();
        simpleService2.out();
        System.out.println("Service2.out");
    }
}
