package io.aermicioi.restcached.spring;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Extracts path information found on a rest controller and it's method and presents it as a list of
 * keys.
 */
public class PathKeyExtractor implements KeyExtractor {

    /**
     * Extract path as a set of segments from a rest controller and it's method.
     * @param point to extract list of keys.
     * @return list of path segments presented as a list of keys.
     */
    @Override
    public List<Object> extractKeys(ProceedingJoinPoint point) {
        final MethodSignature signature = (MethodSignature) point.getSignature();
        final Class<?> targetClass = point.getTarget()
                                          .getClass();

        final ImmutableList.Builder<Object> pathBuilder = ImmutableList.builder();

        RequestMapping classAnnotation = AnnotationUtils.findAnnotation(targetClass,
                                                                        RequestMapping.class);

        if (classAnnotation != null) {
            pathBuilder.add(MergedAnnotations.from(targetClass)
                                             .get(RequestMapping.class)
                                             .getString("path")
                                             .split("/"));
        }

        RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(signature.getMethod(),
                                                                         RequestMapping.class);

        if (methodAnnotation != null) {
            pathBuilder.add(MergedAnnotations.from(signature.getMethod())
                                             .get(RequestMapping.class)
                                             .getString("path")
                                             .split("/"));
        }

        return pathBuilder.build();
    }
}
