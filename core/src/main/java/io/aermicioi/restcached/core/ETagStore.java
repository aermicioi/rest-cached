package io.aermicioi.restcached.core;

import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Storage of ETags by associated keys
 */
public interface ETagStore {

    /**
     * Get ETag that matches following keys.
     *
     * @param keys list of keys that identify stored ETag.
     * @return ETag matching keys or null if not found.
     */
    byte[] get(@NotNull List<Object> keys);

    /**
     * Get ETag that matches following keys.
     *
     * @param keys list of keys that identify stored ETag.
     * @return ETag matching keys or null if not found.
     */
    default byte[] get(@NotNull Object... keys) {
        return this.get(Arrays.asList(keys));
    }

    /**
     * Store ETag identified by provided list of keys
     *
     * @param ETag ETag to store.
     * @param keys list of keys used to identify ETag
     * @return true if tag is stored and associated to following list of keys, false if it already
     * exists.
     */
    boolean set(@NotNull byte[] ETag, @NotNull List<Object> keys);

    /**
     * Store ETag identified by provided list of keys
     *
     * @param ETag ETag to store.
     * @param keys list of keys used to identify ETag
     * @return true if tag is stored and associated to following list of keys, false if it already
     * exists.
     */
    default boolean set(@NotNull byte[] ETag, @NotNull Object... keys) {
        return this.set(ETag, Arrays.asList(keys));
    }

    /**
     * Delete ETag identified by provided list of keys
     * @param keys list of keys used to identify ETag
     * @return true if tag was removed from store, false otherwise
     */
    boolean delete(@NotNull List<Object> keys);

    /**
     * Delete ETag identified by provided list of keys
     * @param keys list of keys used to identify ETag
     * @return true if tag was removed from store, false otherwise
     */
    default boolean delete(@NotNull Object... keys) {
        return this.delete(Arrays.asList(keys));
    }
}
