package io.aermicioi.restcached.jpa

import spock.lang.Specification

import javax.persistence.EntityManager
import java.lang.Object as Shall
import java.time.Instant

class JpaLastModifiedStoreTest extends Specification {

    private EntityManager manager = Mock()

    private JpaLastModifiedStore jpaLastModifiedStore = new JpaLastModifiedStore(manager)

    Shall "manage storing and fetching of last modified"() {
        given: 'a date with key'
        Collection<Object> key =  ['majestic', 20, 'keys']
        Instant current = Instant.now()

        def internal = new LastModified(key.hashCode(), current)
        when: 'saving'
        jpaLastModifiedStore.set(tag, key)

        then: 'manager will be called'
        1 * manager.merge(internal)

        when: 'fetching'
        1 * manager.find(ETag, key.hashCode()) >> internal
        def result = jpaLastModifiedStore.get(key)

        then: 'fetched has same date'
        result == current
    }
}
