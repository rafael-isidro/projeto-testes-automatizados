package com.ada.economizaapi.services.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feature {
    private Properties properties;

    public Feature() {
    }

    public Feature(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}