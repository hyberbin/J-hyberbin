package javax.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Table {

    public String name() default "";

    public String catalog() default "";

    public String schema() default "";

    public UniqueConstraint[] uniqueConstraints() default {};
}
