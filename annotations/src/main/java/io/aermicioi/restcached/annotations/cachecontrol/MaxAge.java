package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The "max-age" response directive indicates that the response is to be considered stale after its
 * age is greater than the specified number of seconds.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.8">Rfc7234</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface MaxAge {

    long value();

    String key() default "__none__";
}
