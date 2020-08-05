package io.aermicioi.restcached.spring.pet;

import java.beans.ConstructorProperties;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public final class Pet {
  @NotNull
  private final String name;

  @NotNull
  private final String type;

  @NotNull
  private final long age;

  @ConstructorProperties({"name", "type", "age"})
  public Pet(String name, String type, long age) {
    this.name = name;
    this.type = type;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public long gETage() {
    return age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pet pet = (Pet) o;
    return age == pet.age &&
        Objects.equals(name, pet.name) &&
        Objects.equals(type, pet.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, age);
  }
}
