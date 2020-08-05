package io.aermicioi.restcached.core;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;

/**
 * In memory storage of ETags by associated keys, backed by concurrent hash map.
 */
public class InMemoryETagStore implements ETagStore {

    private final ConcurrentHashMap<Integer, byte[]> store = new ConcurrentHashMap<>(new HashMap<>());

    @Override
    public byte[] get(@NotNull List<Object> keys) {
        return store.get(keys.hashCode());
    }

    @Override
    public boolean set(@NotNull byte[] ETag, @NotNull List<Object> keys) {
        return store.put(keys.hashCode(), ETag) != null;
    }

    @Override
    public boolean delete(@NotNull List<Object> keys) {
        return store.remove(keys.hashCode()) != null;
    }
}
