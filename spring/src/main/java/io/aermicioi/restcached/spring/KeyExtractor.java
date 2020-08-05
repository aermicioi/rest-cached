package io.aermicioi.restcached.spring;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Extract list of keys from information available in a join point.
 */
public interface KeyExtractor {

    /**
     * Extract a list of keys from join point.
     * @param point to extract list of keys.
     * @return list of keys extracted from join point, or empty list.
     */
    @NotNull
    List<Object> extractKeys(@NotNull ProceedingJoinPoint point);
}
