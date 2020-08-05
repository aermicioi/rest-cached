package io.aermicioi.restcached.core;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;

/**
 * In memory storage of last modified instants by associated keys, backed by concurrent hash map.
 */
public class InMemoryLastModifiedStore implements LastModifiedStore {

    private final ConcurrentHashMap<Integer, Instant> store = new ConcurrentHashMap<>(new HashMap<>());

    @Override
    public Instant get(@NotNull List<Object> keys) {
        return store.get(keys.hashCode());
    }

    @Override
    public boolean set(@NotNull Instant at, @NotNull List<Object> keys) {
        return store.put(keys.hashCode(), at) != null;
    }

    @Override
    public boolean delete(@NotNull List<Object> keys) {
        return store.remove(keys.hashCode()) != null;
    }
}
