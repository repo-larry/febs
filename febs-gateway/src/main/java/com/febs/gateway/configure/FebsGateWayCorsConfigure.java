package com.febs.gateway.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @description:
 * @date: 2022/10/16
 **/
@Configuration
public class FebsGateWayCorsConfigure {

    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.addAllowedOrigin(CorsConfiguration.ALL);
        cors.addAllowedHeader(CorsConfiguration.ALL);
        cors.addAllowedMethod(CorsConfiguration.ALL);
        source.registerCorsConfiguration("/**", cors);
        return new CorsWebFilter(source);
    }

}
