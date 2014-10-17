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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.hyb.mvc.bean.MVCBean;

/**
 * Created by Hyberbin on 2014/7/29.
 */
class MappingMap extends HashMap<String, Object> {

    private static final Logger log = LoggerManager.getLogger(MappingMap.class);
    private static final String[] separates = new String[]{"//", "/","{","}"};
    private static final Set<String> keySet = new TreeSet<String>();
    private static final Map<String,String[]> keySetSplits=new HashMap<String, String[]>();

    private String replaceAll(String str, String old, String news) {
        while (str != null && str.contains(old)) {
            str = str.replace(old, news);
        }
        return str;
    }

    private int compare(String[] cstr1,String[] cstr2){
        if(cstr1.length!=cstr2.length){
            return -1;//完全不匹配
        }
        for(int i=0;i<cstr1.length;i++){
            if(!cstr1[i].equalsIgnoreCase(cstr2[i])){
                return i; //从第i个节点起不匹配
            }
        }
        return -1;
    }

    @Override
    public Object put(String url, Object value) {
        url = replaceAll(url, separates[0], separates[1]);
        keySet.add(url);
        keySetSplits.put(url,url.split(separates[1]));
        return super.put(url, value);
    }

    @Override
    public Object get(Object key) {
        String url=replaceAll(key.toString(), separates[0], separates[1]);
        log.trace("find MVCBean for url:{}",url);
        Set<String> same=new HashSet<String>();
        int max=0;
        String[] splitUrls = url.split(separates[1]);
        for(String keys:keySet){
            if(keys.equalsIgnoreCase(url)){
                log.trace("whole match for url:{}",url);
                return super.get(keys);
            }
            String[] splitKeys = keySetSplits.get(keys);
            int compare = compare(splitUrls, splitKeys);
            if(compare==-1){
                continue;
            }else if(compare>max){
                max=compare;
                same.clear();
                same.add(keys);
            }else if(compare==max&&compare!=0){
                same.add(keys);
            }else if(compare<max){
                break;
            }
        }
        log.trace("find MVCBean for url:{} , size:{}",url,same.size());
        for(String keys:same){
            String[] splitKeys = keySetSplits.get(keys);
            if(splitKeys.length== splitUrls.length){
                MVCBean bean = (MVCBean)super.get(keys);
                Integer[] variables = bean.getVariables();
                boolean matches=true;
                int v=1;
                for (int i = variables[0] + 1; i < splitKeys.length; i++) {
                    if (i == variables[v]) {
                        v++;
                        continue;
                    }
                    if (!splitKeys[i].equalsIgnoreCase(splitUrls[i])) {
                        matches = false;
                        break;
                    }
                }
                if(matches) return bean;
            }
        }
        log.debug("can not find MVCBean for url:{}", url);
        return null;
    }

}
