package io.aermicioi.restcached.spring.blog;

import io.aermicioi.restcached.annotations.Key;
import io.aermicioi.restcached.annotations.LastModified;
import io.aermicioi.restcached.annotations.cachecontrol.MaxAge;
import io.aermicioi.restcached.annotations.cachecontrol.MustRevalidate;
import io.aermicioi.restcached.annotations.cachecontrol.NoCache;
import io.aermicioi.restcached.annotations.cachecontrol.NoStore;
import io.aermicioi.restcached.annotations.cachecontrol.NoTransform;
import io.aermicioi.restcached.annotations.cachecontrol.Private;
import io.aermicioi.restcached.annotations.cachecontrol.ProxyRevalidate;
import io.aermicioi.restcached.annotations.cachecontrol.Public;
import io.aermicioi.restcached.annotations.cachecontrol.SMaxAge;
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
public class BlogStore {

    @NotNull
    private Map<String, Blog> blogs = new HashMap<>();

    public BlogStore() {
    }

    public BlogStore(@NotNull Map<String, Blog> blogs) {
        this.blogs.putAll(blogs);
    }

    @GetMapping("/{:name}")
    @MaxAge(10)
    @MustRevalidate
    @NoTransform
    @Public
    @ProxyRevalidate
    @SMaxAge(20)
    public Blog findBlog(@PathVariable("name") @Key String name) {
        return blogs.get(name);
    }

    @PutMapping("/{:name}")
    @NoStore
    public Blog registerBlog(@PathVariable("name") @Key String name, @RequestBody Blog blog) {
        this.blogs.put(name, blog);
        return blog;
    }

    @PatchMapping("/{:name}")
    @NoCache
    @Private
    public Blog patchBlog(@PathVariable("name") @Key String name, @RequestBody Blog blog) {
        return this.blogs.merge(name, blog, (first, second) -> second);
    }
}
