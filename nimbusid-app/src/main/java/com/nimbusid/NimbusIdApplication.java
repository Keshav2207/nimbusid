package com.nimbusid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NimbusIdApplication {

    public static void main(String[] args) {
        SpringApplication.run(NimbusIdApplication.class, args);
    }
}
