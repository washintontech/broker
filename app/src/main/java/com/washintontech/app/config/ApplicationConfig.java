package com.washintontech.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Import({ClientConfiguration.class,
//        ServerConfiguration.class})
@ComponentScan("com.washintontech.app")
public class ApplicationConfig {
}
