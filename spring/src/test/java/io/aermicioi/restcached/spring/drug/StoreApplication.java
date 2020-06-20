package io.aermicioi.restcached.spring.drug;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableAspectJAutoProxy
@EnableWebMvc
@ComponentScan("io.aermicioi.restcached.spring.drug")
public class StoreApplication {

}
