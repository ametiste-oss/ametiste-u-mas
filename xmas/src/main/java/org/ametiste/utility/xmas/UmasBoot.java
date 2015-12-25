package org.ametiste.utility.xmas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Daria on 29.01.2015.
 */
@Configuration
@EnableAutoConfiguration
@ImportResource("classpath:spring/app-config.xml")
public class UmasBoot {

    public static void main(String[] args) {

        SpringApplication.run(UmasBoot.class, args);

    }


}
