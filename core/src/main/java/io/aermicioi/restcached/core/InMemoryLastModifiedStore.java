package io.aermicioi.restcached.core;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;

public class InMemoryLastModifiedStore implements LastModifiedStore {

    private final ConcurrentHashMap<Integer, Instant> store = new ConcurrentHashMap<>(new HashMap<>());

    @Override
    public Instant get(@NotNull Collection<Object> keys) {
        return store.get(keys.hashCode());
    }

    @Override
    public boolean set(@NotNull Instant at, @NotNull Collection<Object> keys) {
        return store.put(keys.hashCode(), at) != null;
    }
}
