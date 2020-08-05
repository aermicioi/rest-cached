package io.aermicioi.restcached.spring.drug;

import io.aermicioi.restcached.annotations.Key;
import io.aermicioi.restcached.annotations.LastModified;
import java.util.Collection;
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

@RequestMapping("/drug")
@RestController
public class DrugStore {

    @NotNull
    private final Map<String, Drug> drugs = new HashMap<>();

    public DrugStore() {
    }

    public DrugStore(@NotNull Map<String, Drug> drugs) {
        this.drugs.putAll(drugs);
    }

    @GetMapping
    @LastModified
    public Collection<Drug> findDrugs() {
        return drugs.values();
    }

    @GetMapping("/{:name}")
    @LastModified
    public Drug findDrug(@PathVariable("name") @Key String name) {
        return drugs.get(name);
    }

    @PutMapping("/{:name}")
    @LastModified
    public Drug registerDrug(@PathVariable("name") @Key String name, @RequestBody Drug drug) {
        this.drugs.put(name, drug);
        return drug;
    }

    @PatchMapping("/{:name}")
    @LastModified
    public Drug patchDrug(@PathVariable("name") @Key String name, @RequestBody Drug drug) {
        return this.drugs.merge(name, drug, (first, second) -> second);
    }
}
