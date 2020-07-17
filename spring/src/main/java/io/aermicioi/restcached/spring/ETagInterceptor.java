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

@Aspect
public class ETagInterceptor {

    private static final ImmutableSet<RequestMethod> INVOKE_METHODS = ImmutableSet.of(RequestMethod.GET,
                                                                                      RequestMethod.HEAD);
    private static final ImmutableSet<RequestMethod> UPDATE_METHODS = ImmutableSet.of(RequestMethod.DELETE,
                                                                                      RequestMethod.PATCH,
                                                                                      RequestMethod.POST,
                                                                                      RequestMethod.PUT);

    @NotNull
    private final ETagStore eTagStore;

    @NotNull
    private final WebRequest request;

    @NotNull
    private final HttpServletRequest servletRequest;

    public ETagInterceptor(@NotNull ETagStore eTagStore,
                           @NotNull WebRequest request,
                           @NotNull HttpServletRequest servletRequest) {
        this.eTagStore = eTagStore;
        this.request = request;
        this.servletRequest = servletRequest;
    }

    @Around(value = "@annotation(eTag)", argNames = "point,eTag")
    public Object process(ProceedingJoinPoint point,
                          ETag eTag) throws Throwable {
        RequestMethod currentMethod = RequestMethod.valueOf(servletRequest.getMethod()
                                                                          .toUpperCase());

        if (INVOKE_METHODS.contains(currentMethod)) {
            return this.invoke(point, eTag);
        }

        if (UPDATE_METHODS.contains(currentMethod)) {
            return this.update(point, eTag);
        }

        return point.proceed();
    }

    private Object invoke(ProceedingJoinPoint point, ETag eTag) throws Throwable {
        byte[] tag = eTagStore.get(ImmutableList.builder()
                                                  .add(eTag.key())
                                                  .addAll(InterceptorUtils.extractKeys(
                                                      point))
                                                  .build());
        String stringTag = Optional.ofNullable(tag)
                             .map(String::new)
                             .orElse("");

        if (eTag.modifies()) {
            Object result = point.proceed();
            byte[] hash = extractHash(result);

            if (!Arrays.equals(hash, tag)) {
                eTagStore.set(hash,
                              ImmutableList.builder()
                                           .add(eTag.key())
                                           .addAll(InterceptorUtils.extractKeys(point))
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

    private Object update(ProceedingJoinPoint point, ETag eTag) throws Throwable {
        Object result = point.proceed();

        byte[] hash = extractHash(result);

        eTagStore.set(hash,
                      ImmutableList.builder()
                                   .add(eTag.key())
                                   .addAll(InterceptorUtils.extractKeys(point))
                                   .build());

        return result;
    }

    private byte[] extractHash(Object result) {
        int hash = 0;
        if (result != null) {
            hash = result.hashCode();
        }
        return Ints.toByteArray(hash);
    }
}
