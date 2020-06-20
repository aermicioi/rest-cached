package io.aermicioi.restcached.spring.pet;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableAspectJAutoProxy
@EnableWebMvc
@ComponentScan("io.aermicioi.restcached.spring.pet")
public class StoreApplication {

}
