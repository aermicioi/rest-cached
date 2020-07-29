package io.aermicioi.restcached.annotations;

import java.lang.annotation.*;

/**
 * The "Last-Modified" header field in a response provides a timestamp indicating the date and time
 * at which the origin server believes the selected representation was last modified, as determined
 * at the conclusion of handling the request.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.2">Rfc7234</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface LastModified {

    String key() default "__none__";
}
