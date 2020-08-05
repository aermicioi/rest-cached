package io.aermicioi.restcached.jpa;

import io.aermicioi.restcached.core.LastModifiedStore;
import java.time.Instant;
import java.util.List;
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
    public Instant get(@NotNull List<Object> keys) {
        return Optional.ofNullable(manager.find(LastModified.class, keys.hashCode()))
                       .map(LastModified::getInstant)
                       .orElse(null);
    }

    @Override
    public boolean set(@NotNull Instant at, @NotNull List<Object> keys) {
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

    @Override
    public boolean delete(@NotNull List<Object> keys) {
        Optional<LastModified> lastModified = Optional.ofNullable(manager.find(LastModified.class,
                                                                               keys.hashCode()));

        lastModified.ifPresent(manager::remove);

        return lastModified.isPresent();
    }
}
