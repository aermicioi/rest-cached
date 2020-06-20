package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoCache {

    String[] fields() default {};
    String key() default "__none__";
}
