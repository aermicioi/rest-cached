package io.aermicioi.restcached.spring;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.aermicioi.restcached.annotations.LastModified;
import io.aermicioi.restcached.core.LastModifiedStore;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

/**
 * Intercepts method calls annotated with {@link LastModified}. It will circumvent any method calls
 * for a resource that have matching last modified instant from http request. It will update last
 * modified instant for any calls that update resource with newest last modified instant, and return
 * it in response. It will remove last modified instant value for any calls that remove resource.
 */
@Aspect
public class LastModifiedInterceptor {

    private static final ImmutableSet<RequestMethod> INVOKE_METHODS = ImmutableSet.of(RequestMethod.GET,
                                                                                      RequestMethod.HEAD);
    private static final ImmutableSet<RequestMethod> UPDATE_METHODS = ImmutableSet.of(RequestMethod.PATCH,
                                                                                      RequestMethod.POST,
                                                                                      RequestMethod.PUT);
    private static final ImmutableSet<RequestMethod> DELETE_METHODS = ImmutableSet.of(RequestMethod.DELETE);

    @NotNull
    private final LastModifiedStore lastModifiedStore;

    @NotNull
    private final WebRequest request;

    @NotNull
    private final HttpServletRequest servletRequest;

    @NotNull
    private final KeyExtractor extractor;

    /**
     * Construct interceptor enforcing ETag semantics.
     *
     * @param lastModifiedStore storage of last modified instants used to search for current
     *                          instants.
     * @param request           incoming request that potentially supplies an ETag to check.
     * @param servletRequest    incoming request that contains requested method information.
     * @param extractor         key extractor used to extract keys that identify or update last
     *                          modified instants in storage.
     */
    public LastModifiedInterceptor(@NotNull LastModifiedStore lastModifiedStore,
                                   @NotNull WebRequest request,
                                   @NotNull HttpServletRequest servletRequest,
                                   @NotNull KeyExtractor extractor) {
        this.lastModifiedStore = lastModifiedStore;
        this.request = request;
        this.servletRequest = servletRequest;
        this.extractor = extractor;
    }

    /**
     * Intercept method calls annotated with {@link LastModified} and circumvent method call or
     * update/delete last modified instant depending on type of method call.
     * <p>
     * <ul>
     * <li>
     * For idempotent calls it will circumvent subsequent method call if last modified instant found in incoming
     * request matches last modified instant valid for collection of keys that identify requested resource.
     * </li>
     * <li>
     * For update/create calls it will update/create last modified instant and associate it with a collection of keys that identify requested resource.
     * </li>
     * <li>
     * For delete calls it will remove last modified instant value.
     * </li>
     * </ul>
     * </p>
     *
     * @param point        method call used to intercept
     * @param lastModified annotation found on intercepted method call
     * @return result of method call, or null if it is circumvented.
     * @throws Throwable if some exception happened.
     */
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

        if (DELETE_METHODS.contains(currentMethod)) {
            return this.delete(point, lastModified);
        }

        return point.proceed();
    }

    private Object invoke(ProceedingJoinPoint point, LastModified lastModified) throws Throwable {
        Instant tag = lastModifiedStore.get(ImmutableList.builder()
                                                         .add(lastModified.key())
                                                         .addAll(extractor.extractKeys(point))
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
                                           .addAll(extractor.extractKeys(point))
                                           .build());

        return point.proceed();
    }

    private Object delete(ProceedingJoinPoint point, LastModified lastModified) throws Throwable {
        lastModifiedStore.delete(ImmutableList.builder()
                                              .add(lastModified.key())
                                              .addAll(extractor.extractKeys(point))
                                              .build());

        return point.proceed();
    }

}
