package ru.iteco.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.iteco.property.BaseProperties;
import ru.iteco.property.EmailProperties;
import ru.iteco.property.ImageThumbnailProperties;


@Configuration
@ServletComponentScan("ru.iteco.vaadin")
@EnableConfigurationProperties({
        BaseProperties.class,
        EmailProperties.class,
        ImageThumbnailProperties.class
})
@EnableAsync
@EnableCaching
public class ApplicationConfiguration {

}
