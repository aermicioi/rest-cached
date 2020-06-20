package io.aermicioi.restcached.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ETag {
    String key() default "__none__";

    boolean modifies() default false;
}
