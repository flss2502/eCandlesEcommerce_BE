package com.eScentedCandle_project_BackEnd.eScentedCandle.configurations;

import com.lib.payos.PayOS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayOSConfig {
    @Value("${payos.api.secret}")
    private String clientId;
    @Value("${payos.api.key}")
    private String key;
    @Value("${payos.api.check_sum}")
    private String checkSum;

    @Bean
    public PayOS payOS() {
        return new PayOS(clientId, key, checkSum);
    }
}
