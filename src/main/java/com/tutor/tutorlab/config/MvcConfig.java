package com.tutor.tutorlab.config;

import com.tutor.tutorlab.config.converter.enumconverter.EnumerableConverterFactory;
import com.tutor.tutorlab.config.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Bean
    public EnumerableConverterFactory getEnumerableConverterFactory() {
        return new EnumerableConverterFactory();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(getEnumerableConverterFactory());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }

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

