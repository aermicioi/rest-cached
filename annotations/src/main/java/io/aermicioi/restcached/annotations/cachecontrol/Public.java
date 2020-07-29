package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

/**
 * The "public" response directive indicates that any cache MAY store the response, even if the
 * response would normally be non-cacheable or cacheable only within a private cache.  (See Section
 * 3.2 for additional details related to the use of public in response to a request containing
 * Authorization, and Section 3 for details of how public affects responses that would normally not
 * be stored, due to their status codes not being defined as cacheable by default; see Section
 * 4.2.2.)
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.5">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Public {

}
