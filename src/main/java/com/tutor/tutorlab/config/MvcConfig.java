package com.tutor.tutorlab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * CORS 이슈 해결.
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //TODO 매핑설정 세분화해야함.
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3600L);
    }

}