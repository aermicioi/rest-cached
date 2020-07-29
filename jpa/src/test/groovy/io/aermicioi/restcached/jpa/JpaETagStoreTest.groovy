package io.aermicioi.restcached.jpa

import spock.lang.Specification

import javax.persistence.EntityManager
import java.lang.Object as Shall

class JpaETagStoreTest extends Specification {

    private EntityManager manager = Mock()

    private JpaETagStore jpaETagStore = new JpaETagStore(manager)

    Shall "manager storing and fetching of etags"() {
        given: 'a tag with key'
        Collection<Object> key =  ['majestic', 20, 'keys']
        byte[] tag = [1, 1, 1, 1]

        def internal = new ETag(key.hashCode(), tag)
        when: 'saving'
        jpaETagStore.set(tag, key)

        then: 'manager will be called'
        1 * manager.merge(internal)

        when: 'fetching'
        1 * manager.find(ETag, key.hashCode()) >> internal
        def result = jpaETagStore.get(key)

        then: 'fetched is same as tag'
        result == tag
    }
}
