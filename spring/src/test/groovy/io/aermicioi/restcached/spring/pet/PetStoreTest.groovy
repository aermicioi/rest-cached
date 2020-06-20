package io.aermicioi.restcached.spring.pet

import com.google.common.collect.ImmutableMap
import com.google.common.primitives.Ints
import io.aermicioi.restcached.core.ETagStore
import io.aermicioi.restcached.core.InMemoryETagStore
import io.aermicioi.restcached.spring.ETagInterceptor
import io.aermicioi.restcached.spring.pet.Pet
import io.aermicioi.restcached.spring.pet.PetStore
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import org.springframework.web.context.request.WebRequest
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest

class PetStoreTest extends Specification {

    private HttpServletRequest servletRequest = Mock()

    private WebRequest request = Mock()

    private ETagStore store = new InMemoryETagStore()

    private PetStore petController = new PetStore(ImmutableMap.of("fiona", new Pet("fiona", "cat", 4)))

    private ETagInterceptor aspect

    private AspectJProxyFactory factory

    def setup() {
        aspect = new ETagInterceptor(store, request, servletRequest)
        factory = new AspectJProxyFactory(petController)
        factory.addAspect(aspect)
    }

    @Unroll
    def "find pet that is #cached and return #result"() {
        given:
        store.set("test".bytes, "__none__", "fiona")

        PetStore proxy = factory.proxy
        when:
        1 * servletRequest.getMethod() >> "GET"
        1 * request.checkNotModified("test") >> notModified

        def pet = proxy.findPet("fiona")

        then:
        pet == expectedResult

        where:
        cached       | notModified || expectedResult             || result
        "cached"     | true        || null                       || "cached notification"
        "not cached" | false       || new Pet("fiona", "cat", 4) || "pet from registry"
    }

    @Unroll
    def "find pet that is possibly #cached and return #result"() {
        given:
        PetStore proxy = factory.proxy
        when:
        1 * servletRequest.getMethod() >> "GET"
        1 * request.checkNotModified(new String(Ints.toByteArray(leo.hashCode()))) >> notModified

        def pet = proxy.materializePet("leo", leo)

        then:
        pet == expectedResult
        store.get("__none__", "leo") == Ints.toByteArray(leo.hashCode())

        where:
        cached       | notModified || expectedResult           || result
        "cached"     | true        || null                     || "cached notification"
        "not cached" | false       || new Pet("leo", "cat", 2) || "pet from registry"
        "not cached" | false       || new Pet("leo", "cat", 2) || "pet from registry"

        leo = new Pet("leo", "cat", 2)
    }

    @Unroll
    def "register pet"() {
        given:
        PetStore proxy = factory.proxy
        when:
        1 * servletRequest.getMethod() >> "PUT"

        def pet = proxy.registerPet("murzik", murzik)

        then:
        pet == expectedResult
        store.get("__none__", "murzik") == Ints.toByteArray(murzik.hashCode())

        where:
        cached    || expectedResult              || result
        "created" || new Pet("murzik", "cat", 6) || "cached notification"

        murzik = new Pet("murzik", "cat", 6)
    }

    @Unroll
    def "update information about #original.name"() {
        given:
        PetStore proxy = factory.proxy

        when: "on first registration"
        1 * servletRequest.getMethod() >> "PUT"
        def pet = proxy.registerPet(original.name, original)

        then:
        pet == original
        store.get("__none__", original.name) == Ints.toByteArray(original.hashCode())

        when: "on update"
        1 * servletRequest.getMethod() >> "PATCH"
        pet = proxy.patchPet(updated.name, updated)

        then:
        pet == updated
        store.get("__none__", updated.name) == Ints.toByteArray(updated.hashCode())

        where:
        original = new Pet("murzik", "cat", 6)
        updated = new Pet("murzik", "cat", 8)
    }
}
