package io.aermicioi.restcached.core;

import io.aermicioi.restcached.annotations.cachecontrol.*;

import javax.validation.constraints.NotNull;
import java.time.Duration;

public interface CacheControlBuilder {
    @NotNull
    CacheControlBuilder maxAge(@NotNull MaxAge maxAge);

    @NotNull
    CacheControlBuilder maxAge(@NotNull Duration delta);

    @NotNull
    CacheControlBuilder mustRevalidate(@NotNull MustRevalidate mustRevalidate);

    @NotNull
    CacheControlBuilder mustRevalidate();

    @NotNull
    CacheControlBuilder noCache(@NotNull NoCache noCache);

    @NotNull
    CacheControlBuilder noCache(@NotNull String... fields);

    @NotNull
    CacheControlBuilder noStore(@NotNull NoStore noStore);

    @NotNull
    CacheControlBuilder noStore();

    @NotNull
    CacheControlBuilder noTransform(@NotNull NoTransform noTransform);

    @NotNull
    CacheControlBuilder noTransform();

    @NotNull
    CacheControlBuilder priv(@NotNull Private priv);

    @NotNull
    CacheControlBuilder priv(@NotNull String... fields);

    @NotNull
    CacheControlBuilder proxyRevalidate(@NotNull ProxyRevalidate proxyRevalidate);

    @NotNull
    CacheControlBuilder proxyRevalidate();

    @NotNull
    CacheControlBuilder pub(@NotNull Public pub);

    @NotNull
    CacheControlBuilder pub();

    @NotNull
    CacheControlBuilder sMaxAge(@NotNull SMaxAge sMaxAge);

    @NotNull
    CacheControlBuilder sMaxAge(@NotNull Duration delta);

    String build();
}
