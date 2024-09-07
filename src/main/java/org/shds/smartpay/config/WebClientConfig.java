package org.shds.smartpay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${company.url}")
    private String company_url;

    @Bean
    public WebClient companyAPI() {
        return WebClient.builder()
<<<<<<< HEAD
                .baseUrl("http://52.79.43.173:8092") //카드사 서버
=======
                .baseUrl(company_url) //카드사 서버
>>>>>>> bafaeaf (fix: 적용 #4)
                .build();
    }
}
