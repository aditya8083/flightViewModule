package com.coviam.controller;

import com.coviam.entity.FlightSearchResponse;
import com.coviam.service.FlightSearchManager;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class FlightSearchController {

    @Autowired
    FlightSearchManager flightSearchManager;


    @RequestMapping(value = "/flight/search", method = RequestMethod.GET)
    public String flightSearch(HttpServletRequest request) throws JSONException {
        System.out.println("Getting all Flights");
        String searchResponse = flightSearchManager.getAllFlights(request);
        return searchResponse;
    }

    @RequestMapping(value = "/flight/saveFlightSearchResponse", method = RequestMethod.POST)
    public String saveFlightSearchResponse(@RequestParam("origin") String origin, @RequestParam("destination") String destination,
                                           @RequestParam("isRefundable") boolean isRefundable, @RequestParam("originDepartDate") String originDepartDate,
                                           @RequestParam("originDepartTime") String originDepartTime, @RequestParam("destinationArrivalDate") String destinationArrivalDate,
                                           @RequestParam("destinationArrivalTime") String destinationArrivalTime, @RequestParam("flightName") String flightName,
                                           @RequestParam("flightNumber") String flightNumber, @RequestParam("seatRemain") String seatRemain,
                                           @RequestParam("pricePerAdult") String pricePerAdult, @RequestParam("pricePerChild") String pricePerChild,
                                           @RequestParam("pricePerInfant") String pricePerInfant, Model model) {
        System.out.println("Getting a Flight data");
        model.addAttribute("origin", origin);  model.addAttribute("destination", destination);
        model.addAttribute("isRefundable", isRefundable);  model.addAttribute("originDepartDate", originDepartDate);
        model.addAttribute("originDepartTime", originDepartTime);  model.addAttribute("destinationArrivalDate", destinationArrivalDate);
        model.addAttribute("destinationArrivalTime", destinationArrivalTime);  model.addAttribute("flightName", flightName);
        model.addAttribute("flightNumber", flightNumber);  model.addAttribute("seatRemain", seatRemain);
        model.addAttribute("pricePerAdult", pricePerAdult);  model.addAttribute("pricePerChild", pricePerChild);
        model.addAttribute("pricePerInfant", pricePerInfant);
        flightSearchManager.saveFlightSearchResponse(getAllFlightDataValues(origin, destination, isRefundable, originDepartDate,
                originDepartTime, destinationArrivalDate, destinationArrivalTime, flightName, flightNumber,  seatRemain,
                pricePerAdult, pricePerChild, pricePerInfant));
        return "Flight Data saved successfully";
    }

    private FlightSearchResponse getAllFlightDataValues(String origin, String destination, boolean isRefundable,
                                                        String originDepartDate, String originDepartTime,
                                                        String destinationArrivalDate, String destinationArrivalTime,
                                                        String flightName, String flightNumber, String seatRemain,
                                                        String pricePerAdult, String pricePerChild, String pricePerInfant) {
        FlightSearchResponse flightSearchResponse = new FlightSearchResponse();
        flightSearchResponse.setOrigin(origin.toUpperCase());
        flightSearchResponse.setDestination(destination.toUpperCase());
        flightSearchResponse.setRefundable(isRefundable);
        flightSearchResponse.setOriginDepartDate(originDepartDate);
        flightSearchResponse.setOriginDepartTime(originDepartTime);
        flightSearchResponse.setDestinationArrivalDate(destinationArrivalDate);
        flightSearchResponse.setDestinationArrivalTime(destinationArrivalTime);
        flightSearchResponse.setFlightName(flightName);
        flightSearchResponse.setFlightNumber(flightNumber);
        flightSearchResponse.setSeatRemain(seatRemain);
        flightSearchResponse.setPricePerAdult(pricePerAdult);
        flightSearchResponse.setPricePerChild(pricePerChild);
        flightSearchResponse.setPricePerInfant(pricePerInfant);
        return flightSearchResponse;
    }


}