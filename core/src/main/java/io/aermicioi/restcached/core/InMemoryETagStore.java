package io.aermicioi.restcached.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;

public class InMemoryETagStore implements ETagStore {

    private final ConcurrentHashMap<Integer, byte[]> store = new ConcurrentHashMap<>(new HashMap<>());

    @Override
    public byte[] get(@NotNull Collection<Object> keys) {
        return store.get(keys.hashCode());
    }

    @Override
    public boolean set(@NotNull byte[] tag, @NotNull Collection<Object> keys) {
        return store.put(keys.hashCode(), tag) != null;
    }
}
