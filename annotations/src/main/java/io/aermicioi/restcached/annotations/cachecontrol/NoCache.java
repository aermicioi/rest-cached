package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

/**
 * The "no-cache" response directive indicates that the response MUST NOT be used to satisfy a
 * subsequent request without successful validation on the origin server.  This allows an origin
 * server to prevent a cache from using it to satisfy a request without contacting it, even by
 * caches that have been configured to send stale responses.
 * <p>
 * If the no-cache response directive specifies one or more field-names, then a cache MAY use the
 * response to satisfy a subsequent request, subject to any other restrictions on caching.  However,
 * any header fields in the response that have the field-name(s) listed MUST NOT be sent in the
 * response to a subsequent request without successful revalidation with the origin server.  This
 * allows an origin server to prevent the re-use of certain header fields in a response, while still
 * allowing caching of the rest of the response.
 * <p>
 * The field-names given are not limited to the set of header fields defined by this specification.
 * Field names are case-insensitive.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.2">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoCache {

    String[] fields() default {};

    String key() default "__none__";
}
