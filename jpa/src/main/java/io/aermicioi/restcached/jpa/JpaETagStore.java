package io.aermicioi.restcached.jpa;

import io.aermicioi.restcached.core.ETagStore;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

/**
 * Jpa storage of ETags by associated keys
 */
public class JpaETagStore implements ETagStore {

    @NotNull
    private final EntityManager manager;

    public JpaETagStore(@NotNull EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public byte[] get(@NotNull List<Object> keys) {
        return Optional.ofNullable(manager.find(ETag.class, keys.hashCode()))
                       .map(ETag::getETag)
                       .orElse(null);
    }

    @Override
    public boolean set(@NotNull byte[] tag, @NotNull List<Object> keys) {
        ETag ETag = Optional.ofNullable(manager.find(ETag.class, keys.hashCode()))
                            .orElse(null);

        if (ETag != null && Arrays.equals(tag, ETag.getETag())) {
            return false;
        }

        if (ETag == null) {
            ETag = new ETag(keys.hashCode(), tag);
        }

        manager.merge(ETag);

        return true;
    }

    @Override
    public boolean delete(@NotNull List<Object> keys) {
        Optional<ETag> eTag = Optional.ofNullable(manager.find(ETag.class, keys.hashCode()));
        eTag.ifPresent(manager::remove);

        return eTag.isPresent();
    }
}
