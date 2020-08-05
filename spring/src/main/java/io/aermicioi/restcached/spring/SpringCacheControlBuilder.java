package io.aermicioi.restcached.spring;

import io.aermicioi.restcached.annotations.cachecontrol.MaxAge;
import io.aermicioi.restcached.annotations.cachecontrol.MustRevalidate;
import io.aermicioi.restcached.annotations.cachecontrol.NoCache;
import io.aermicioi.restcached.annotations.cachecontrol.NoStore;
import io.aermicioi.restcached.annotations.cachecontrol.NoTransform;
import io.aermicioi.restcached.annotations.cachecontrol.Private;
import io.aermicioi.restcached.annotations.cachecontrol.ProxyRevalidate;
import io.aermicioi.restcached.annotations.cachecontrol.Public;
import io.aermicioi.restcached.annotations.cachecontrol.SMaxAge;
import io.aermicioi.restcached.core.CacheControlBuilder;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import javax.validation.constraints.NotNull;
import org.springframework.http.CacheControl;

/**
 * Builder of Cache-Control header that delegates {@link CacheControl} for actual building of header value.
 */
public class SpringCacheControlBuilder implements CacheControlBuilder {

    @NotNull
    private CacheControl control;

    @Override
    public @NotNull CacheControlBuilder maxAge(@NotNull MaxAge maxAge) {
        return this.maxAge(Duration.of(maxAge.value(), ChronoUnit.SECONDS));
    }

    @Override
    public @NotNull CacheControlBuilder maxAge(@NotNull Duration delta) {
        control = CacheControl.maxAge(delta);

        return this;
    }

    @Override
    public @NotNull CacheControlBuilder mustRevalidate(@NotNull MustRevalidate mustRevalidate) {
        return this.mustRevalidate();
    }

    @Override
    public @NotNull CacheControlBuilder mustRevalidate() {
        control.mustRevalidate();

        return this;
    }

    @Override
    public @NotNull CacheControlBuilder noCache(@NotNull NoCache noCache) {
        return this.noCache();
    }

    @Override
    public @NotNull CacheControlBuilder noCache(@NotNull String... fields) {
        control = CacheControl.noCache();
        return this;
    }

    @Override
    public @NotNull CacheControlBuilder noStore(@NotNull NoStore noStore) {
        return this.noStore();
    }

    @Override
    public @NotNull CacheControlBuilder noStore() {
        control = CacheControl.noStore();
        return this;
    }

    @Override
    public @NotNull CacheControlBuilder noTransform(@NotNull NoTransform noTransform) {
        return this.noTransform();
    }

    @Override
    public @NotNull CacheControlBuilder noTransform() {
        control.noTransform();
        return this;
    }

    @Override
    public @NotNull CacheControlBuilder priv(@NotNull Private priv) {
        return this.priv(priv.fields());
    }

    @Override
    public @NotNull CacheControlBuilder priv(@NotNull String... fields) {
        control.cachePrivate();
        return this;
    }

    @Override
    public @NotNull CacheControlBuilder proxyRevalidate(@NotNull ProxyRevalidate proxyRevalidate) {
        this.proxyRevalidate();
        return this;
    }

    @Override
    public @NotNull CacheControlBuilder proxyRevalidate() {
        control.proxyRevalidate();
        return this;
    }

    @Override
    public @NotNull CacheControlBuilder pub(@NotNull Public pub) {
        return this.pub();
    }

    @Override
    public @NotNull CacheControlBuilder pub() {
        control.cachePublic();
        return this;
    }

    @Override
    public @NotNull CacheControlBuilder sMaxAge(@NotNull SMaxAge sMaxAge) {
        return this.sMaxAge(Duration.of(sMaxAge.value(), ChronoUnit.SECONDS));
    }

    @Override
    public @NotNull CacheControlBuilder sMaxAge(@NotNull Duration delta) {
        control.sMaxAge(delta);
        return this;
    }

    @Override
    public String build() {
        return control.getHeaderValue();
    }
}
