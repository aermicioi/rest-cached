package io.aermicioi.restcached.spring.blog

import io.aermicioi.restcached.spring.CacheControlInterceptor
import io.aermicioi.restcached.spring.SpringCacheControlBuilder
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import org.springframework.http.HttpHeaders
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse
import java.time.Instant

class BlogStoreTest extends Specification {
    private HttpServletResponse servletResponse = Mock()

    private BlogStore blogStore = new BlogStore()

    private CacheControlInterceptor aspect

    private AspectJProxyFactory factory

    def "will properly set the cache"() {
        given:
        aspect = new CacheControlInterceptor(servletResponse, SpringCacheControlBuilder::new)
        factory = new AspectJProxyFactory(blogStore)
        factory.addAspect(aspect)
        BlogStore proxy = factory.proxy
        def restCachedBlog = new Blog("rest-cached", "technology", Instant.now())
        def patchedBlog = new Blog("rest-cached", "science", Instant.now())

        when: "registering the blog"
        1 * servletResponse.setHeader(HttpHeaders.CACHE_CONTROL, "no-store")

        proxy.registerBlog("rest-cached", restCachedBlog)
        then:
        blogStore.findBlog("rest-cached") == restCachedBlog

        when: "patching the blog"
        1 * servletResponse.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, private")

        proxy.patchBlog("rest-cached", patchedBlog);
        then:
        blogStore.findBlog("rest-cached") == patchedBlog

        when: "fetching the blog"
        1 * servletResponse.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=10, must-revalidate, no-transform, public, proxy-revalidate, s-maxage=20")

        def result = proxy.findBlog("rest-cached")
        then:
        result == patchedBlog
    }
}
