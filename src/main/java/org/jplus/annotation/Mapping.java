package org.jplus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mode说明.
 * @author Hyberbin
 * @date 2013-6-8 12:56:50
 */
@Target({java.lang.annotation.ElementType.METHOD})//该注解只能用在成员变量上
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

    String name() default "default";

    boolean isDefault() default false;
}
