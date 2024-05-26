package com.ada.economizaapi.services.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Segment {
    private Double distance;

    public Segment(){
    }

    public Segment(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }
}