package io.aermicioi.restcached.core;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import javax.validation.constraints.NotNull;

public interface LastModifiedStore {
    Instant get(@NotNull Collection<Object> keys);
    default Instant get(@NotNull Object... keys) {
        return this.get(Arrays.asList(keys));
    }

    boolean set(@NotNull Instant at, @NotNull Collection<Object> keys);
    default boolean set(@NotNull Instant at, @NotNull Object... keys) {
        return this.set(at, Arrays.asList(keys));
    }
}
