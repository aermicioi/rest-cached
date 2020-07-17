module io.aermicioi.restcached.spring {
    requires spring.web;
    requires com.google.common;
    requires org.aspectj.weaver;
    requires java.validation;
    requires transitive io.aermicioi.restcached.core;
    requires java.servlet;
    requires jakarta.inject;
    exports io.aermicioi.restcached.spring;
}