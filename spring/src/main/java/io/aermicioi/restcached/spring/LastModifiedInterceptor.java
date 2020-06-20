package io.aermicioi.restcached.spring;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.aermicioi.restcached.annotations.LastModified;
import io.aermicioi.restcached.core.LastModifiedStore;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Aspect
public class LastModifiedInterceptor {

    private static final ImmutableSet<RequestMethod> INVOKE_METHODS = ImmutableSet.of(RequestMethod.GET,
                                                                                      RequestMethod.HEAD);
    private static final ImmutableSet<RequestMethod> UPDATE_METHODS = ImmutableSet.of(RequestMethod.DELETE,
                                                                                      RequestMethod.PATCH,
                                                                                      RequestMethod.POST,
                                                                                      RequestMethod.PUT);

    @NotNull
    private final LastModifiedStore lastModifiedStore;

    @NotNull
    private final WebRequest request;

    @NotNull
    private final HttpServletRequest servletRequest;

    public LastModifiedInterceptor(@NotNull LastModifiedStore lastModifiedStore,
                                   @NotNull WebRequest request,
                                   @NotNull HttpServletRequest servletRequest) {
        this.lastModifiedStore = lastModifiedStore;
        this.request = request;
        this.servletRequest = servletRequest;
    }

    @Around(value = "@annotation(lastModified)", argNames = "point,lastModified")
    public Object process(ProceedingJoinPoint point,
                          LastModified lastModified) throws Throwable {
        RequestMethod currentMethod = RequestMethod.valueOf(servletRequest.getMethod()
                                                                          .toUpperCase());

        if (INVOKE_METHODS.contains(currentMethod)) {
            return this.invoke(point, lastModified);
        }

        if (UPDATE_METHODS.contains(currentMethod)) {
            return this.update(point, lastModified);
        }

        return point.proceed();
    }

    private Object invoke(ProceedingJoinPoint point, LastModified lastModified) throws Throwable {
        Instant tag = lastModifiedStore.get(ImmutableList.builder()
                                                         .add(lastModified.key())
                                                         .addAll(InterceptorUtils.extractKeys(point))
                                                         .build()); // TODO null safety

        if (request.checkNotModified(tag.toEpochMilli())) {
            return null;
        }

        return point.proceed();
    }

    private Object update(ProceedingJoinPoint point, LastModified lastModified) throws Throwable {
        lastModifiedStore.set(Instant.now(),
                              ImmutableList.builder()
                                           .add(lastModified.key())
                                           .addAll(InterceptorUtils.extractKeys(point))
                                           .build());

        return point.proceed();
    }

}
