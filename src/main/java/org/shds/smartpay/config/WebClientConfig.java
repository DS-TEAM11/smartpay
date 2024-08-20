package org.shds.smartpay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient companyAPI() {
        return WebClient.builder()
                .baseUrl("http://localhost:8092") //카드사 서버
                .build();
    }
}
