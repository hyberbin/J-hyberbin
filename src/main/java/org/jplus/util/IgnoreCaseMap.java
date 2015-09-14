/*
 * Copyright 2015 www.hyberbin.com.
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

import java.util.HashMap;

/**
 *
 * @author hyberbin
 */
public class IgnoreCaseMap<K, V> extends HashMap<K, V> {

    public IgnoreCaseMap() {
        super();
    }

    public IgnoreCaseMap(K key, V v) {
        put(key, v);
    }

    public IgnoreCaseMap linkPut(K key, V value) {
        put(key, value);
        return this;
    }

    @Override
    public V get(Object key) {
        return super.get(key instanceof String?key.toString().toLowerCase():key);
    }

    @Override
    public V put(K key, V value) {
        return super.put(key instanceof String?(K) key.toString().toLowerCase():key, value);
    }

}
