package io.aermicioi.restcached.jpa;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Entry that associates last modified instant to a hash of an object/objects
 */
@Entity
class LastModified {

    @Id
    private int hash;

    @NotNull
    private Instant instant;

    private LastModified() {
    }

    /**
     * Construct an entry with hash & instant
     * @param hash of object associated to instant
     * @param instant last modified instant
     */
    public LastModified(int hash, @NotNull Instant instant) {
        this.hash = hash;
        this.instant = instant;
    }

    /**
     * Get hash of associated object
     * @return hash
     */
    public int getHash() {
        return hash;
    }

    /**
     * Set hash of associated object
     * @param hash hash of new object to associate
     */
    public void setHash(int hash) {
        this.hash = hash;
    }

    /**
     * Get last modified instant
     * @return last modified instant
     */
    public Instant getInstant() {
        return instant;
    }

    /**
     * Set last modified instant
     * @param instant new last modified instant
     */
    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LastModified that = (LastModified) o;
        return hash == that.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }
}
