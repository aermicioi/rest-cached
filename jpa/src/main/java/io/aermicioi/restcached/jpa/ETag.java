package io.aermicioi.restcached.jpa;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * ETag entry which associates ETag to a hash of an object.
 */
@Entity
class ETag {

    @Id
    private int hash;

    @NotNull
    private byte[] eTag;

    private ETag() {
    }

    /**
     * Construct ETag with hash & tag
     * @param hash used to identify tag
     * @param eTag tag associated to hash
     */
    public ETag(int hash, @NotNull byte[] eTag) {
        this.hash = hash;
        this.eTag = eTag;
    }

    /**
     * Get hash associated to tag
     * @return hash associated to tag
     */
    public int getHash() {
        return hash;
    }

    /**
     * Set hash associated to tag
     * @param hash associated to tag
     */
    public void setHash(int hash) {
        this.hash = hash;
    }

    /**
     * Get ETag.
     * @return ETag
     */
    public byte[] getETag() {
        return eTag;
    }

    /**
     * Set ETag
     * @param eTag new ETag value
     */
    public void setETag(byte[] eTag) {
        this.eTag = eTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ETag ETag = (ETag) o;
        return hash == ETag.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }
}
