package io.aermicioi.restcached.spring;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import io.aermicioi.restcached.annotations.Key;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Extracts values from parameters annotated with {@link Key}.
 */
class StandardKeyExtractor implements KeyExtractor {

    /**
     * {@inheritDoc} It will extract values of parameters annotated with {@link Key} found on method
     * from join point and return them as extracted keys.
     */
    public ImmutableList<Object> extractKeys(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();

        if (signature == null) {
            return ImmutableList.of();
        }

        Annotation[][] parameterAnnotations = signature.getMethod()
                                                       .getParameterAnnotations();

        return Streams.zip(Arrays.stream(point.getArgs()),
                           Arrays.stream(parameterAnnotations),
                           Map::entry)
                      .filter(entry -> Arrays.stream(entry.getValue())
                                             .anyMatch(Key.class::isInstance))
                      .map(Map.Entry::getKey)
                      .collect(ImmutableList.toImmutableList());
    }
}
