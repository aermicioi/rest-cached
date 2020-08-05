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
import jakarta.inject.Provider;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;

/**
 * Intercepts method calls with annotations from {@link io.aermicioi.restcached.annotations.cachecontrol}
 * and builds & sets Cache-Control header in http response.
 */
@Aspect
public class CacheControlInterceptor {

    @NotNull
    private final HttpServletResponse response;

    @NotNull
    private final Provider<CacheControlBuilder> builderFactory;

    /**
     * Construct cache-control interceptor with response & cache-control builder
     *
     * @param response       used to set resulting cache-control header
     * @param builderFactory factory creating cache-control builder used to build header set in
     *                       response.
     */
    public CacheControlInterceptor(@NotNull HttpServletResponse response,
                                   @NotNull Provider<CacheControlBuilder> builderFactory) {
        this.response = response;
        this.builderFactory = builderFactory;
    }

    /**
     * Intercept method call and start building cache-control header with max-age directive as
     * basis.
     *
     * @param point  interception point.
     * @param maxAge annotation found on intercepted point.
     */
    @Before(value = "@annotation(maxAge)", argNames = "point,maxAge")
    public void intercept(JoinPoint point, MaxAge maxAge) {
        this.configureAndSet(point,
                             this.builderFactory.get()
                                                .maxAge(maxAge));
    }

    /**
     * Intercept method call and start building cache-control header with no-cache directive as
     * basis.
     *
     * @param point   interception point.
     * @param noCache annotation found in intercepted point.
     */
    @Before(value = "@annotation(noCache)", argNames = "point,noCache")
    public void intercept(JoinPoint point, NoCache noCache) {
        this.configureAndSet(point,
                             this.builderFactory.get()
                                                .noCache(noCache));
    }

    /**
     * Intercept method call and start building cache-control header with no-store directive as
     * basis.
     *
     * @param point   interception point.
     * @param noStore annotation found in intercepted point.
     */
    @Before(value = "@annotation(noStore)", argNames = "point,noStore")
    public void intercept(JoinPoint point, NoStore noStore) {
        this.configureAndSet(point,
                             this.builderFactory.get()
                                                .noStore(noStore));
    }

    private void configureAndSet(JoinPoint point, CacheControlBuilder builder) {
        MethodSignature signature = (MethodSignature) point.getSignature();

        if (signature != null) {
            Optional.ofNullable(signature.getMethod()
                                         .getAnnotation(MustRevalidate.class))
                    .ifPresent(builder::mustRevalidate);
            Optional.ofNullable(signature.getMethod()
                                         .getAnnotation(NoTransform.class))
                    .ifPresent(builder::noTransform);
            Optional.ofNullable(signature.getMethod()
                                         .getAnnotation(Private.class))
                    .ifPresent(builder::priv);
            Optional.ofNullable(signature.getMethod()
                                         .getAnnotation(ProxyRevalidate.class))
                    .ifPresent(builder::proxyRevalidate);
            Optional.ofNullable(signature.getMethod()
                                         .getAnnotation(Public.class))
                    .ifPresent(builder::pub);
            Optional.ofNullable(signature.getMethod()
                                         .getAnnotation(SMaxAge.class))
                    .ifPresent(builder::sMaxAge);
        }

        this.response.setHeader(HttpHeaders.CACHE_CONTROL, builder.build());
    }
}
