package io.aermicioi.restcached.spring.pet;

import io.aermicioi.restcached.annotations.ETag;
import io.aermicioi.restcached.annotations.Key;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pet")
@RestController
public class PetStore {

    @NotNull
    private Map<String, Pet> pets = new HashMap<>();

    public PetStore() {
    }

    public PetStore(@NotNull Map<String, Pet> pets) {
        this.pets.putAll(pets);
    }

    @GetMapping("/{:name}")
    @ETag
    public Pet findPet(@PathVariable("name") @Key String name) {
        return pets.get(name);
    }

    @GetMapping
    @ETag(modifies = true)
    public Pet materializePet(@PathVariable("name") @Key String name, @RequestBody Pet pet) {
        pets.putIfAbsent(name, pet);

        return pets.get(name);
    }

    @PutMapping("/{:name}")
    @ETag
    public Pet registerPet(@PathVariable("name") @Key String name, @RequestBody Pet pet) {
        this.pets.put(name, pet);
        return pet;
    }

    @PatchMapping("/{:name}")
    @ETag
    public Pet patchPet(@PathVariable("name") @Key String name, @RequestBody Pet pet) {
        return this.pets.merge(name, pet, (first, second) -> second);
    }
}
