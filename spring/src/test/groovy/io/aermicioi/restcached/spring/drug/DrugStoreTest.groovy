package io.aermicioi.restcached.spring.drug


import io.aermicioi.restcached.core.InMemoryLastModifiedStore
import io.aermicioi.restcached.core.LastModifiedStore
import io.aermicioi.restcached.spring.LastModifiedInterceptor
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import org.springframework.web.context.request.WebRequest
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import java.time.Instant

class DrugStoreTest extends Specification {
    private HttpServletRequest servletRequest = Mock()

    private WebRequest request = Mock()

    private LastModifiedStore store = new InMemoryLastModifiedStore()

    private DrugStore drugStore = new DrugStore()

    private LastModifiedInterceptor aspect

    private AspectJProxyFactory factory

    def "register, fetch, update, last modified timestamp for each type of request"() {
        given:
        aspect = new LastModifiedInterceptor(store, request, servletRequest)
        factory = new AspectJProxyFactory(drugStore)
        factory.addAspect(aspect)
        DrugStore proxy = factory.proxy
        def bionell = new Drug("bionell", "harmless", Instant.now())
        def update = new Drug(bionell.name, "not harmless", bionell.release.plusSeconds(60))

        when: "register a drug"
        1 * servletRequest.method >> "PUT"

        def registered = proxy.registerDrug(bionell.name, bionell)
        def registrationInstant = store.get("__none__", "bionell")
        then: "registration is equal or after release date"
        registered == bionell
        with(registrationInstant) {
            it.isAfter(bionell.release)
        }

        when: "fetching for first time drug"
        1 * servletRequest.method >> "GET"
        1 * request.checkNotModified(_ as Long) >> false

        def firstFetched = proxy.findDrug(bionell.name)
        then: "return the actual drug"
        firstFetched == bionell

        when: "fetching for second time drug"
        1 * servletRequest.method >> "GET"
        1 * request.checkNotModified(_ as Long) >> true

        def secondFetched = proxy.findDrug(bionell.name)
        then: "answer not modified"
        secondFetched == null

        when: "updating information"
        1 * servletRequest.method >> "PATCH"

        def updatedVersion = proxy.patchDrug(bionell.name, update)
        then: "return newer version"
        updatedVersion == update

        when: "fetching updated version"
        1 * servletRequest.method >> "GET"
        1 * request.checkNotModified(_ as Long) >> false

        def fetchedUpdatedVersion = proxy.findDrug(update.name)
        then: "answer with updated drug"
        fetchedUpdatedVersion == update

        with(store.get("__none__", update.name)) {
            it.isAfter(bionell.release)
        }
    }
}
