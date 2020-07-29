package io.aermicioi.restcached.annotations;

import java.lang.annotation.*;

/**
 * The "Expires" header field gives the date/time after which the response is considered stale.  See
 * Section 4.2 for further discussion of the freshness model.
 * <p>
 * The presence of an Expires field does not imply that the original resource will change or cease
 * to exist at, before, or after that time.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.3">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Expires {

    String key() default "__none__";
}
