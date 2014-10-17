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
package org.jplus.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 对象的相关操作工具帮助类.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 12:54 PM
 * @since JDK 1.5
 */
public class ObjectHelper {


	public static boolean isNullOrEmptyString(Object o) {
		if(o == null)
			return true;
		if(String.class.isAssignableFrom(o.getClass())) {
			String str = (String)o;
			if(str.trim().length() == 0)
				return true;
		}
		return false;
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否为空
	 * @param o
	 * @return
	 */
	@SuppressWarnings("all")
	public static boolean isEmpty(Object o)  {
		if(o == null) return true;

		if(String.class.isAssignableFrom(o.getClass())) {
			if(((String)o).length() == 0){
				return true;
			}
		} else if(Collection.class.isAssignableFrom(o.getClass())) {
			if(((Collection)o).isEmpty()){
				return true;
			}
		} else if(o.getClass().isArray()) {
			if(Array.getLength(o) == 0){
				return true;
			}
		} else if(Map.class.isAssignableFrom(o.getClass())) {
			if(((Map)o).isEmpty()){
				return true;
			}
		}else {
			return false;
		}

		return false;
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object c) throws IllegalArgumentException{
		return !isEmpty(c);
	}
}
