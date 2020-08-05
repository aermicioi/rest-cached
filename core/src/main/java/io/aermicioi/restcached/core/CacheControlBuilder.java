package io.aermicioi.restcached.core;

import io.aermicioi.restcached.annotations.cachecontrol.MaxAge;
import io.aermicioi.restcached.annotations.cachecontrol.MustRevalidate;
import io.aermicioi.restcached.annotations.cachecontrol.NoCache;
import io.aermicioi.restcached.annotations.cachecontrol.NoStore;
import io.aermicioi.restcached.annotations.cachecontrol.NoTransform;
import io.aermicioi.restcached.annotations.cachecontrol.Private;
import io.aermicioi.restcached.annotations.cachecontrol.ProxyRevalidate;
import io.aermicioi.restcached.annotations.cachecontrol.Public;
import io.aermicioi.restcached.annotations.cachecontrol.SMaxAge;
import java.time.Duration;
import javax.validation.constraints.NotNull;

/**
 * Constructs a cache control header out of provided information.
 */
public interface CacheControlBuilder {

    /**
     * Configure max age using {@link MaxAge} annotation
     *
     * @param maxAge annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder maxAge(@NotNull MaxAge maxAge);

    /**
     * Set max age to delta value
     *
     * @param delta max age duration
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder maxAge(@NotNull Duration delta);

    /**
     * Set must revalidate using {@link MustRevalidate} annotation
     *
     * @param mustRevalidate annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder mustRevalidate(@NotNull MustRevalidate mustRevalidate);

    /**
     * Add must revalidate to cache control
     *
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder mustRevalidate();

    /**
     * Set no cache using {@link NoCache} annotation
     *
     * @param noCache annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder noCache(@NotNull NoCache noCache);

    /**
     * Set no cache with following fields removed from cached response.
     *
     * @param fields list of fields that should not be served from cache response, without
     *               re-validation from origin server.
     * @return builder for cache-control header
     */
    @NotNull
    CacheControlBuilder noCache(@NotNull String... fields);

    /**
     * Set no store using {@link NoStore} annotation
     *
     * @param noStore annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder noStore(@NotNull NoStore noStore);

    /**
     * Add no store to cache control
     *
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder noStore();

    /**
     * Set no transform using {@link NoTransform} annotation
     *
     * @param noTransform annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder noTransform(@NotNull NoTransform noTransform);

    /**
     * Add no transform to cache control
     *
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder noTransform();

    /**
     * Set private using {@link Private} annotation
     *
     * @param priv annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder priv(@NotNull Private priv);

    /**
     * Set private for following fields or entire response
     *
     * @param fields http header fields used that are marked as private
     * @return builder for cache-control header
     */
    @NotNull
    CacheControlBuilder priv(@NotNull String... fields);

    /**
     * Set proxy revalidate using {@link ProxyRevalidate} annotation
     *
     * @param proxyRevalidate annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder proxyRevalidate(@NotNull ProxyRevalidate proxyRevalidate);

    /**
     * Add proxy revalidate to cache control
     *
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder proxyRevalidate();

    /**
     * Set public using {@link Public} annotation
     *
     * @param pub annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder pub(@NotNull Public pub);

    /**
     * Add public to cache control
     *
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder pub();

    /**
     * Set s-max age using {@link SMaxAge} annotation
     *
     * @param sMaxAge annotation containing required information
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder sMaxAge(@NotNull SMaxAge sMaxAge);

    /**
     * Set s-max age to cache control
     *
     * @param delta amount of time to set.
     * @return builder for cache-control header.
     */
    @NotNull
    CacheControlBuilder sMaxAge(@NotNull Duration delta);

    /**
     * Build resulting cache-control header value.
     *
     * @return cache-control header value.
     */
    String build();
}
