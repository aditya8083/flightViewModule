package com.coviam.entity;

import org.springframework.stereotype.Component;

@Component
public class FlightPricingRequest {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FlightPricingRequest{" + "id=" + id + '}';
    }
}
