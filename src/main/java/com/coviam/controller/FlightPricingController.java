package com.coviam.controller;


import com.coviam.service.FlightPricingManager;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightPricingController {

    @Autowired FlightPricingManager flightPricingManager;

    @RequestMapping(value = "/flight/price", method = RequestMethod.GET)
    public String flightPricing(HttpServletRequest request) {
        System.out.println("Getting Flight Pricing");
        String pricingResponse = flightPricingManager.getFlightPricing(request);
        return pricingResponse;
    }
}
