package io.aermicioi.restcached.annotations.cachecontrol;

import java.lang.annotation.*;

/**
 * The "no-store" response directive indicates that a cache MUST NOT store any part of either the
 * immediate request or response.  This directive applies to both private and shared caches.  "MUST
 * NOT store" in this context means that the cache MUST NOT intentionally store the information in
 * non-volatile storage, and MUST make a best-effort attempt to remove the information from volatile
 * storage as promptly as possible after forwarding it.
 * <p>
 * This directive is NOT a reliable or sufficient mechanism for ensuring privacy.  In particular,
 * malicious or compromised caches might not recognize or obey this directive, and communications
 * networks might be vulnerable to eavesdropping.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2.2.3">Rfc7234</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoStore {

    String key() default "__none__";
}
