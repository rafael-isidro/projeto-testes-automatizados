package com.ada.economizaapi.services.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistanceResponse {
    private Feature[] features;

    public DistanceResponse() {}

    public void setFeatures(Feature[] features) {
        this.features = features;
    }
}
