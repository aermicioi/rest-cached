package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

/**
 * The "must-revalidate" response directive indicates that once it has become stale, a cache MUST
 * NOT use the response to satisfy subsequent requests without successful validation on the origin
 * server.
 * <p>
 * The must-revalidate directive is necessary to support reliable operation for certain protocol
 * features.  In all circumstances a cache MUST obey the must-revalidate directive; in particular,
 * if a cache cannot reach the origin server for any reason, it MUST generate a 504 (Gateway
 * Timeout) response.
 * <p>
 * The must-revalidate directive ought to be used by servers if and only if failure to validate a
 * request on the representation could result in incorrect operation, such as a silently unexecuted
 * financial transaction.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.1">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MustRevalidate {

}
