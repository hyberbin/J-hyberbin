/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.hyb.mvc.mapping;

import org.jplus.hyb.mvc.bean.MVCBean;

/**
 *
 * @author Hyberbin
 */
public interface IMappingManager {

    public void putMapping(String url, MVCBean bean);

    public void putBefore(String url, MVCBean bean);

    public void putAfter(String url, MVCBean bean);

    public MVCBean getMapping(String url);

    public MVCBean getBefore(String url);

    public MVCBean getAfter(String url);

    public String getPathVariable(MVCBean bean,String url,int index);
}
