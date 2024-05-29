package io.hydrocarbon.moutai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@ConfigurationPropertiesScan(basePackages = "io.hydrocarbon.moutai.property")
public class FuckIMoutaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuckIMoutaiApplication.class, args);
    }
}
