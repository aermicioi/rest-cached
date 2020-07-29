package io.aermicioi.restcached.annotations;

import java.lang.annotation.*;

/**
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ETag {
    String key() default "__none__";

    boolean modifies() default false;
}
