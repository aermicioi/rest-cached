package io.aermicioi.restcached.jpa;

import io.aermicioi.restcached.core.LastModifiedStore;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

public class JpaLastModifiedStore implements LastModifiedStore {

    @NotNull
    private final EntityManager manager;

    public JpaLastModifiedStore(@NotNull EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Instant get(@NotNull Collection<Object> keys) {
        return Optional.ofNullable(manager.find(LastModified.class, keys.hashCode()))
                       .map(LastModified::getInstant)
                       .orElse(null);
    }

    @Override
    public boolean set(@NotNull Instant at, @NotNull Collection<Object> keys) {
        LastModified lastModified = Optional.ofNullable(manager.find(LastModified.class, keys.hashCode()))
                            .orElse(null);

        if (lastModified != null && at.equals(lastModified.getInstant())) {
            return false;
        }

        if (lastModified == null) {
            lastModified = new LastModified(keys.hashCode(), at);
        }

        manager.merge(lastModified);

        return true;
    }
}
