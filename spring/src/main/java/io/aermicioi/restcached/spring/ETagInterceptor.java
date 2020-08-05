package io.aermicioi.restcached.spring;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Ints;
import io.aermicioi.restcached.annotations.ETag;
import io.aermicioi.restcached.core.ETagStore;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

/**
 * Intercepts method calls annotated with {@link ETag}. It will circumvent any method calls for a
 * resource that have matching ETag from http request that is still valid. It will update ETag value
 * for any calls that update resource with newest ETag value, and return it in response. It will
 * remove ETag value for any calls that remove resource.
 */
@Aspect
public class ETagInterceptor {

    private static final ImmutableSet<RequestMethod> INVOKE_METHODS = ImmutableSet.of(RequestMethod.GET,
                                                                                      RequestMethod.HEAD);
    private static final ImmutableSet<RequestMethod> UPDATE_METHODS = ImmutableSet.of(RequestMethod.PATCH,
                                                                                      RequestMethod.POST,
                                                                                      RequestMethod.PUT);
    private static final ImmutableSet<RequestMethod> DELETE_METHODS = ImmutableSet.of(RequestMethod.DELETE);

    @NotNull
    private final ETagStore ETagStore;

    @NotNull
    private final WebRequest request;

    @NotNull
    private final HttpServletRequest servletRequest;

    @NotNull
    private final KeyExtractor extractor;

    /**
     * Construct interceptor enforcing ETag semantics.
     *
     * @param ETagStore      storage of ETags used to search for valid ETag values.
     * @param request        incoming request that potentially supplies an ETag to check.
     * @param servletRequest incoming request that contains requested method information.
     * @param extractor      key extractor used to extract keys that identify or update ETag values
     *                       in storage.
     */
    public ETagInterceptor(@NotNull ETagStore ETagStore,
                           @NotNull WebRequest request,
                           @NotNull HttpServletRequest servletRequest,
                           @NotNull KeyExtractor extractor) {
        this.ETagStore = ETagStore;
        this.request = request;
        this.servletRequest = servletRequest;
        this.extractor = extractor;
    }

    /**
     * Intercept method calls annotated with {@link ETag} and circumvent method call or
     * update/delete ETag depending on type of method call.
     * <p>
     * <ul>
     * <li>
     * For idempotent calls it will circumvent subsequent method call if ETag found in incoming
     * request matches ETag that is still valid for collection of keys that identify requested resource.
     * </li>
     * <li>
     * For update/create calls it will update/create ETag value and associate it with a collection of keys that identify requested resource.
     * </li>
     * <li>
     * For delete calls it will remove ETag value.
     * </li>
     * </ul>
     * </p>
     *
     * @param point intercepted method call
     * @param ETag  found on intercepted method call
     * @return result of method call or null if it is circumvented.
     * @throws Throwable if some exception happened.
     */
    @Around(value = "@annotation(ETag)", argNames = "point,ETag")
    public Object process(ProceedingJoinPoint point,
                          ETag ETag) throws Throwable {
        RequestMethod currentMethod = RequestMethod.valueOf(servletRequest.getMethod()
                                                                          .toUpperCase());

        if (INVOKE_METHODS.contains(currentMethod)) {
            return this.invoke(point, ETag);
        }

        if (UPDATE_METHODS.contains(currentMethod)) {
            return this.update(point, ETag);
        }

        if (DELETE_METHODS.contains(currentMethod)) {
            return this.delete(point, ETag);
        }

        return point.proceed();
    }

    private Object invoke(ProceedingJoinPoint point, ETag ETag) throws Throwable {
        byte[] tag = ETagStore.get(ImmutableList.builder()
                                                .add(ETag.key())
                                                .addAll(extractor.extractKeys(point))
                                                .build());
        String stringTag = Optional.ofNullable(tag)
                                   .map(String::new)
                                   .orElse("");

        if (ETag.modifies()) {
            Object result = point.proceed();
            byte[] hash = extractHash(result);

            if (!Arrays.equals(hash, tag)) {
                ETagStore.set(hash,
                              ImmutableList.builder()
                                           .add(ETag.key())
                                           .addAll(extractor.extractKeys(point))
                                           .build());
                stringTag = new String(hash);
            }

            if (request.checkNotModified(stringTag)) {
                return null;
            }

            return result;
        }

        if (request.checkNotModified(stringTag)) {
            return null;
        }

        return point.proceed();
    }

    private Object update(ProceedingJoinPoint point, ETag ETag) throws Throwable {
        Object result = point.proceed();

        byte[] hash = extractHash(result);

        ETagStore.set(hash,
                      ImmutableList.builder()
                                   .add(ETag.key())
                                   .addAll(extractor.extractKeys(point))
                                   .build());

        return result;
    }

    private Object delete(ProceedingJoinPoint point, ETag ETag) throws Throwable {
        ETagStore.delete(ImmutableList.builder()
                                      .add(ETag.key(), extractor.extractKeys(point))
                                      .build());

        return point.proceed();
    }

    private byte[] extractHash(Object result) {
        int hash = 0;
        if (result != null) {
            hash = result.hashCode();
        }
        return Ints.toByteArray(hash);
    }
}
