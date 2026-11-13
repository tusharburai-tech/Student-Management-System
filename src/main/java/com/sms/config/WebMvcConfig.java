package com.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/", "/dashboard/**", "/students/**", "/courses/**", "/attendance/**", "/reports/**", "/settings/**")
                .excludePathPatterns("/login", "/logout", "/css/**", "/js/**", "/images/**", "/h2-console/**", "/error");
    }
}
