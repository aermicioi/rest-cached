package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

/**
 * The "no-transform" response directive indicates that an intermediary (regardless of whether it
 * implements a cache) MUST NOT transform the payload, as defined in Section 5.7.2 of [RFC7230].
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.4">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoTransform {

}
