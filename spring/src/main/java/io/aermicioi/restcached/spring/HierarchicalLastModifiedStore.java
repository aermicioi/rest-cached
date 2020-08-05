package io.aermicioi.restcached.spring;

import io.aermicioi.restcached.core.LastModifiedStore;
import java.time.Instant;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Storage of last modified instants with hierarchy semantics for associated keys.
 *
 * A last modified instant will be matched for passed list of keys, or any sublist of it starting from first key.
 */
public class HierarchicalLastModifiedStore implements LastModifiedStore {

    @NotNull
    private final LastModifiedStore store;

    /**
     * Constructs a {@link HierarchicalLastModifiedStore} with {@link LastModifiedStore store} to which actual storage is delegated.
     * @param store to which actual storage is delegated.
     */
    public HierarchicalLastModifiedStore(@NotNull LastModifiedStore store) {
        this.store = store;
    }

    @Override
    public Instant get(@NotNull List<Object> keys) {
        return store.get(keys);
    }

    /**
     * {@inheritDoc}
     * It will also associate last modified instant to any sublist of keys starting from first key element.
     */
    @Override
    public boolean set(@NotNull Instant lastModified, @NotNull List<Object> keys) {
        for (int size = 1; size < keys.size() - 1; ++size) {
            store.set(lastModified, keys.subList(0, size));
        }

        return store.set(lastModified, keys);
    }

    @Override
    public boolean delete(@NotNull List<Object> keys) {
        return store.delete(keys);
    }
}
