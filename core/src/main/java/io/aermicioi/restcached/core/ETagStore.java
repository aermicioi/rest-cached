package io.aermicioi.restcached.core;

import java.util.Arrays;
import java.util.Collection;
import javax.validation.constraints.NotNull;

public interface ETagStore {
    byte[] get(@NotNull Collection<Object> keys);

    default byte[] get(@NotNull Object... keys) {
        return this.get(Arrays.asList(keys));
    }

    boolean set(@NotNull byte[] tag, @NotNull Collection<Object> keys);

    default boolean set(@NotNull byte[] tag, @NotNull Object... keys) {
        return this.set(tag, Arrays.asList(keys));
    }
}
