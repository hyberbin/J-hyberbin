package org.jplus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Before说明.
 * @author Hyberbin
 * @date 2013-6-8 15:38:50
 */
@Target({java.lang.annotation.ElementType.METHOD})//该注解只能用在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
    /**失败后跳转的页面*/
    String send();
    /**失败信息*/
    String message() default "";
}
