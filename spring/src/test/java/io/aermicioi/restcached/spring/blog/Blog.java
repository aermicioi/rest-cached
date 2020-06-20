package io.aermicioi.restcached.spring.blog;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public final class Blog {
  @NotNull
  private final String name;

  @NotNull
  private final String classification;

  @NotNull
  private final Instant release;

  @ConstructorProperties({"name", "type", "release"})
  public Blog(String name, String classification, Instant release) {
    this.name = name;
    this.classification = classification;
    this.release = release;
  }

  public String getName() {
    return name;
  }

  public String getClassification() {
    return classification;
  }

  public Instant getRelease() {
    return release;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Blog blog = (Blog) o;
    return Objects.equals(name, blog.name) &&
        Objects.equals(classification, blog.classification) &&
        Objects.equals(release, blog.release);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, classification, release);
  }
}
