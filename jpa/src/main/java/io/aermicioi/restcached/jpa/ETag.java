package io.aermicioi.restcached.jpa;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
class ETag {

    @Id
    private int hash;

    @NotNull
    private byte[] tag;

    private ETag() {
    }

    public ETag(int hash, @NotNull byte[] tag) {
        this.hash = hash;
        this.tag = tag;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public byte[] getTag() {
        return tag;
    }

    public void setTag(byte[] tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ETag eTag = (ETag) o;
        return hash == eTag.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }
}
