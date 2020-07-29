package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

/**
 * The "s-maxage" response directive indicates that, in shared caches, the maximum age specified by
 * this directive overrides the maximum age specified by either the max-age directive or the Expires
 * header field.  The s-maxage directive also implies the semantics of the proxy-revalidate response
 * directive.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.9">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SMaxAge {

    long value();
}
