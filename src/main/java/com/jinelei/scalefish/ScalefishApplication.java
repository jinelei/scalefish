package com.jinelei.scalefish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ScalefishApplication {

    private static final Logger log = LoggerFactory.getLogger(ScalefishApplication.class);

    public static void main(String[] args) {
        log.info("Starting Scalefish Application...");
        SpringApplication.run(ScalefishApplication.class, args);
        log.info("Scalefish Application started");
    }
}
