package io.aermicioi.restcached.spring;

import io.aermicioi.restcached.annotations.cachecontrol.*;
import io.aermicioi.restcached.core.CacheControlBuilder;
import jakarta.inject.Provider;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Aspect
public class CacheControlInterceptor {

    @NotNull
    private final HttpServletResponse response;

    @NotNull
    private final Provider<CacheControlBuilder> builderFactory;

    public CacheControlInterceptor(@NotNull HttpServletResponse response,
                                   @NotNull Provider<CacheControlBuilder> builderFactory) {
        this.response = response;
        this.builderFactory = builderFactory;
    }

    @Before(value = "@annotation(maxAge)", argNames = "point,maxAge")
    public void check(JoinPoint point, MaxAge maxAge) {
        this.configureAndSet(point, this.builderFactory.get().maxAge(maxAge));
    }

    @Before(value = "@annotation(noCache)", argNames = "point,noCache")
    public void check(JoinPoint point, NoCache noCache) {
        this.configureAndSet(point, this.builderFactory.get().noCache(noCache));
    }

    @Before(value = "@annotation(noStore)", argNames = "point,noStore")
    public void check(JoinPoint point, NoStore noStore) {
        this.configureAndSet(point, this.builderFactory.get().noStore(noStore));
    }

    public void configureAndSet(JoinPoint point, CacheControlBuilder builder) {
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
