package io.aermicioi.restcached.core;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Storage of last modified instants associated by keys.
 */
public interface LastModifiedStore {

    /**
     * Get an instant associated by a collection of keys.
     * @param keys collection of keys that is associated
     * @return Instant associated to collection of keys.
     */
    Instant get(@NotNull List<Object> keys);

    /**
     * Get an instant associated by a collection of keys.
     * @param keys collection of keys that is associated
     * @return Instant associated to collection of keys.
     */
    default Instant get(@NotNull Object... keys) {
        return this.get(Arrays.asList(keys));
    }

    /**
     * Associate an instant by a collection of keys
     * @param at instant associated to collection of keys
     * @param keys collection of keys used to identify instant
     * @return true if instant was stored, false otherwise
     */
    boolean set(@NotNull Instant at, @NotNull List<Object> keys);

    /**
     * Associate an instant by a collection of keys
     * @param at instant associated to collection of keys
     * @param keys collection of keys used to identify instant
     * @return true if instant was stored, false otherwise
     */
    default boolean set(@NotNull Instant at, @NotNull Object... keys) {
        return this.set(at, Arrays.asList(keys));
    }

    /**
     * Delete last modified instant identified by provided list of keys
     * @param keys list of keys used to identify last modified instant
     * @return true if instant was removed from store, false otherwise
     */
    boolean delete(@NotNull List<Object> keys);

    /**
     * Delete last modified instant identified by provided list of keys
     * @param keys list of keys used to identify last modify instant
     * @return true if instant was removed from store, false otherwise
     */
    default boolean delete(@NotNull Object... keys) {
        return this.delete(Arrays.asList(keys));
    }
}
