package org.jplus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Hyberbin
 */
@Target({java.lang.annotation.ElementType.TYPE})//该注解只能用在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

    String name();

    boolean asyncSupported() default false;

    String[] urlPatterns();
}
