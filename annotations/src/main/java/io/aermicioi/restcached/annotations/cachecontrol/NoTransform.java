package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoTransform {
}
