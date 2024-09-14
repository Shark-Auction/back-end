package com.exe.sharkauction.components.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns(allowedOrigins)
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(false)
//                .maxAge(MAX_AGE_SECS);
//    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        //  Set routes that allow cross domain routing
//        registry.addMapping("/**")
//                //  Set the domain name that allows cross domain requests
//                //.allowedOrigins("*")
//                // Cross domain configuration error , take .allowedOrigins Replace with .allowedOriginPatterns that will do .
//                .allowedOriginPatterns("*")
//                //  Whether to allow certificates （cookies）
//                .allowCredentials(true)
//                //  Set allowed methods
//                .allowedMethods("*")
//                //  Cross domain allow time
//                .maxAge(3600);
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOriginPattern("*");
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
//        config.addAllowedHeader("*");
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }


    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "responseType", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
