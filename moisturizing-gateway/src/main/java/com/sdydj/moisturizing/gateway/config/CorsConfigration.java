package com.sdydj.moisturizing.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsConfigration {

    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration =new  CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");//头
        corsConfiguration.addAllowedMethod("*");//请求方式
        corsConfiguration.addAllowedOrigin("*");//请求来源
        corsConfiguration.setAllowCredentials(true);//允许携带cookie

        source.registerCorsConfiguration("/**",corsConfiguration);
        CorsWebFilter corsWebFilter = new CorsWebFilter(source);

        return corsWebFilter;
    }
}