package com.ada.economizaapi.services.responses;

public class Properties {
    private Segment[] segments;

    public Properties() {
    }

    public Properties(Segment[] segments) {
        this.segments = segments;
    }

    public Segment[] getSegments() {
        return segments;
    }
}
