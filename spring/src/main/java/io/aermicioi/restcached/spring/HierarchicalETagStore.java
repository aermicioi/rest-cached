package io.aermicioi.restcached.spring;

import io.aermicioi.restcached.core.ETagStore;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Storage of ETags with hierarchy semantics for associated keys.
 *
 * An ETag will be matched for passed list of keys, or any sublist of it starting from first key.
 */
public class HierarchicalETagStore implements ETagStore {

    @NotNull
    private final ETagStore store;

    /**
     * Constructs a {@link HierarchicalETagStore} with {@link ETagStore store} to which actual storage is delegated.
     * @param store to which actual storage is delegated.
     */
    public HierarchicalETagStore(@NotNull ETagStore store) {
        this.store = store;
    }

    @Override
    public byte[] get(@NotNull List<Object> keys) {
        return store.get(keys);
    }

    /**
     * {@inheritDoc}
     * It will also associate ETag to any sublist of keys starting from first key element.
     */
    @Override
    public boolean set(@NotNull byte[] ETag, @NotNull List<Object> keys) {
        for (int size = 1; size < keys.size() - 1; ++size) {
            store.set(ETag, keys.subList(0, size));
        }

        return store.set(ETag, keys);
    }

    @Override
    public boolean delete(@NotNull List<Object> keys) {
        return store.delete(keys);
    }
}
