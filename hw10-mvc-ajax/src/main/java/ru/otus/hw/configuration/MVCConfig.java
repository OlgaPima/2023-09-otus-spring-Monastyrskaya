package ru.otus.hw.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/static/components/**")
                .addResourceLocations("classpath:/static/components/");
        registry.addResourceHandler("/static/script/**")
                .addResourceLocations("classpath:/static/script/");
        registry.addResourceHandler("/static/icons/**")
                .addResourceLocations("classpath:/static/icons/");
    }
}