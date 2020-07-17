package io.aermicioi.restcached.jpa;

import io.aermicioi.restcached.core.ETagStore;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

public class JpaETagStore implements ETagStore {

    @NotNull
    private final EntityManager manager;

    public JpaETagStore(@NotNull EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public byte[] get(@NotNull Collection<Object> keys) {
        return Optional.ofNullable(manager.find(ETag.class, keys.hashCode()))
                       .map(ETag::getTag)
                       .orElse(null);
    }

    @Override
    public boolean set(@NotNull byte[] tag, @NotNull Collection<Object> keys) {
        ETag eTag = Optional.ofNullable(manager.find(ETag.class, keys.hashCode()))
                            .orElseGet(ETag::new);

        if (Arrays.equals(tag, eTag.getTag())) {
            return false;
        }

        eTag.setTag(tag);
        manager.merge(eTag);

        return true;
    }
}
