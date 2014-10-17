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

import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;

/**
 *
 * @author Hyberbin
 */
public class NullUtils {
     private static final Logger log = LoggerManager.getLogger(NullUtils.class);

    public static boolean isNUll(Object o) {
        return null == o;
    }

    public static void validateNull(Object o) {
        if (isNUll(o)) {
            log.debug("validateNull object is null!");
            throw new IllegalArgumentException();
        }
    }
    public static void validateNull(Object o,String name) {
        if (isNUll(o)) {
            log.debug("validateNull {} is null!",name);
            throw new IllegalArgumentException();
        }
    }

}
