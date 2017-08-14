package ru.iteco.config;

import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import ru.iteco.FlatBlockTemplateMethod;
import ru.iteco.property.BaseProperties;

@Configuration
public class FreemarkerConfiguration {

    @Autowired
    public FreemarkerConfiguration(BaseProperties baseProperties,
                                   FlatBlockTemplateMethod flatBlockTemplateMethod,
                                   FreeMarkerAutoConfiguration.FreeMarkerWebConfiguration webConfiguration)
            throws TemplateModelException {
        freemarker.template.Configuration configuration = webConfiguration.freeMarkerConfigurer().getConfiguration();
        configuration.setSharedVariable("flatBlock", flatBlockTemplateMethod);
        configuration.setSharedVariable("mediaUrl", baseProperties.getMediaUrl());
    }
}
