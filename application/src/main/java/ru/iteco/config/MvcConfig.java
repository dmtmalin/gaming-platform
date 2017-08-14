package ru.iteco.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Integer cacheDays = 365;

        // minify and combine js/css (!GENERATE WHEN RUN "mvn clean package" SEE pom.xml)
        registry.addResourceHandler("/minify/**")
                .addResourceLocations("classpath:/static/minify/")
                .setCacheControl(CacheControl.maxAge(cacheDays, TimeUnit.DAYS));

        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/")
                .setCacheControl(CacheControl.maxAge(cacheDays, TimeUnit.DAYS));

        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/")
                .setCacheControl(CacheControl.maxAge(cacheDays, TimeUnit.DAYS));
    }
}
