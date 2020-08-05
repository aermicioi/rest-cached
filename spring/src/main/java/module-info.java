module io.aermicioi.restcached.spring {
    requires spring.web;
    requires com.google.common;
    requires org.aspectj.weaver;
    requires java.validation;
    requires transitive io.aermicioi.restcached.core;
    requires java.servlet;
    requires jakarta.inject;
    requires spring.core;
    opens io.aermicioi.restcached.spring;
}