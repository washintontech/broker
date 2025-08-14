package com.washintontech.app;

import com.washintontech.app.config.ApplicationConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@Import(ApplicationConfig.class)
@EnableWebFlux
public class AppApplication {

    private static final Logger log = LogManager.getLogger(AppApplication.class);

    public static void main(String[] args) {
        try {
            final TimeZone jvmTimeZone = TimeZone.getTimeZone(ZoneId.of("UTC"));
            TimeZone.setDefault(jvmTimeZone);
            Locale.setDefault(Locale.US);

            final var applicationContext = SpringApplication.run(AppApplication.class, args);
            applicationContext.addApplicationListener(new ShutDownListener());

            log.info("Matching Engine Client Started....  ");

//            log.info("""
//
//                        Matching Engine Client Started....        \s
//                    """);

        } catch (Exception exception) {
            log.error("Failed to start Matching Engine Client: ", exception);
            System.exit(1);
        }
    }

}
