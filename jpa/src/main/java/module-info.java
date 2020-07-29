module io.aermicioi.restcached.jpa {
    requires java.persistence;
    requires java.validation;
    requires transitive io.aermicioi.restcached.core;
    opens io.aermicioi.restcached.jpa;
}