package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

/**
 * The "private" response directive indicates that the response message is intended for a single
 * user and MUST NOT be stored by a shared cache.  A private cache MAY store the response and reuse
 * it for later requests, even if the response would normally be non-cacheable.
 * <p>
 * If the private response directive specifies one or more field-names, this requirement is limited
 * to the field-values associated with the listed response header fields.  That is, a shared cache
 * MUST NOT store the specified field-names(s), whereas it MAY store the remainder of the response
 * message.
 * <p>
 * The field-names given are not limited to the set of header fields defined by this specification.
 * Field names are case-insensitive.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.6">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Private {

    String[] fields() default {};
}
