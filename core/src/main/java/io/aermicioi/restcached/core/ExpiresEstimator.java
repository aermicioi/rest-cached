package io.aermicioi.restcached.core;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Estimates after how much time an object expires.
 */
public interface ExpiresEstimator {

    /**
     * Checks whether estimated object is supported by estimator.
     * @param clazz type of object to estimate.
     * @return true if it is able to estimate, false otherwise.
     */
    boolean supports(@NotNull Class<?> clazz);

    /**
     * Checks whether estimated object is supported by estimator.
     * @param object object to estimate.
     * @return true if it is able to estimate, false otherwise.
     */
    default boolean supports(@NotNull Object object) {
        Objects.requireNonNull(object);

        return this.supports(object.getClass());
    }

    /**
     * Estimate expiration time of object.
     * @param object object to estimate
     * @param keys list of keys used to identify object, and possibly used in estimation.
     * @return Instant when object expires.
     */
    @NotNull
    Instant estimate(@NotNull Object object, @NotNull Collection<Object> keys);

    /**
     * Estimate expiration time of object.
     * @param object object to estimate
     * @param keys list of keys used to identify object, and possibly used in estimation.
     * @return Instant when object expires.
     */
    @NotNull
    default Instant estimate(@NotNull Object object, @NotNull Object... keys) {
        return this.estimate(object, Arrays.asList(keys));
    }
}
