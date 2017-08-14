package ru.iteco.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.email")
public class EmailProperties {

    private Integer expirationHour = 24;

    public Integer getExpirationHour() {
        return expirationHour;
    }

    public void setExpirationHour(Integer expirationHour) {
        this.expirationHour = expirationHour;
    }
}
