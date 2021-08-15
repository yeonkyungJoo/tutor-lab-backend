package com.tutor.tutorlab.config;

import com.tutor.tutorlab.config.converter.enumconverter.EnumerableConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public EnumerableConverterFactory getEnumerableConverterFactory() {
        return new EnumerableConverterFactory();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(getEnumerableConverterFactory());
    }
}
