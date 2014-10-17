/*
 * Copyright 2014 Hyberbin.
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

