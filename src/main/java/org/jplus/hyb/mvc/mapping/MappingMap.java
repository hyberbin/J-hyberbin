package org.jplus.hyb.mvc.mapping;

import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.hyb.mvc.bean.MVCBean;

import java.util.*;

/**
 * Created by Hyberbin on 2014/7/29.
 */
class MappingMap extends HashMap<String, Object> {

    private final static Logger log = LoggerManager.getLogger(MappingMap.class);
    private final static String[] separates = new String[]{"//", "/","{","}"};
    private final static Set<String> keySet = new TreeSet<String>();
    private final static Map<String,String[]> keySetSplits=new HashMap<String, String[]>();

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
