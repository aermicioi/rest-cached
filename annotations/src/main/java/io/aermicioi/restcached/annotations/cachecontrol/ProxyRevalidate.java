package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

/**
 * The "proxy-revalidate" response directive has the same meaning as the must-revalidate response
 * directive, except that it does not apply to private caches.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.7">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProxyRevalidate {

}
