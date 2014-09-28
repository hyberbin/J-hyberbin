/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.mvc.mapping;

import java.util.HashMap;
import java.util.Map;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.hyb.mvc.bean.MVCBean;

/**
 *
 * @author Hyberbin
 */
public class MappingManager implements IMappingManager {

    private static final Logger log = LoggerManager.getLogger(MappingManager.class);

    private static final Map mapping = new MappingMap();
    private static final Map<String, MVCBean> before = new HashMap<String, MVCBean>();
    private static final Map<String, MVCBean> after = new HashMap<String, MVCBean>();
    private static final String separates ="/";

    @Override
    public void putMapping(String url, MVCBean bean) {
        mapping.put(url, bean);
    }

    @Override
    public MVCBean getMapping(String url) {
        return (MVCBean) mapping.get(url);
    }

    @Override
    public void putBefore(String url, MVCBean bean) {
        before.put(url, bean);
    }

    @Override
    public void putAfter(String url, MVCBean bean) {
        after.put(url, bean);
    }

    @Override
    public MVCBean getBefore(String url) {
        return before.get(url);
    }

    @Override
    public MVCBean getAfter(String url) {
        return after.get(url);
    }

    @Override
    public String getPathVariable(MVCBean bean,String url, int index) {
        return url.split(separates)[bean.getVariables()[index]];
    }
}

