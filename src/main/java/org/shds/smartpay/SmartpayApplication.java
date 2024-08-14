package org.shds.smartpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SmartpayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartpayApplication.class, args);
    }

}
