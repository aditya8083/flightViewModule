package com.coviam.controller;

import com.coviam.entity.FlightSearchResponse;
import com.coviam.service.FlightSearchManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class FlightSearchController {

    @Autowired
    FlightSearchManager flightSearchManager;


    @RequestMapping(value = "/flight/search", method = RequestMethod.GET)
    public String flightSearch(@RequestParam("origin") String origin, @RequestParam("destination") String destination,
                               @RequestParam("originDepartDate") String originDepartDate, @RequestParam("destinationArrivalDate") String destinationArrivalDate,
                               @RequestParam("adults") String adults, @RequestParam("infants") String infants,
                               @RequestParam("children") String children, @RequestParam("flightType") String flightType,
                                Model model) throws JSONException {
        System.out.println("Getting all Flights");
        model.addAttribute("origin", origin);  model.addAttribute("destination", destination);
        model.addAttribute("originDepartDate", originDepartDate);  model.addAttribute("destinationArrivalDate", destinationArrivalDate);
        model.addAttribute("adults", adults);  model.addAttribute("infants", infants);
        model.addAttribute("children", children);  model.addAttribute("flightType", flightType);

        String searchResponse = flightSearchManager.getAllFlights(origin, destination, originDepartDate, destinationArrivalDate, adults, infants, children, flightType);
        Map<Integer,List<FlightSearchResponse>> flightMap = transformedFlightSearchRes(searchResponse);
        model.addAttribute("flightResult", flightMap);
        return "flightSearchResult";
    }



    private static FlightSearchResponse toEntity(String jsonString)
    {
        try{
            Gson gson = new GsonBuilder().create();
            FlightSearchResponse flightSearchInfo = gson.fromJson(jsonString, FlightSearchResponse.class);
            return flightSearchInfo;
        }
        catch(JsonSyntaxException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }


    private static Map<Integer,List<FlightSearchResponse>> transformedFlightSearchRes(String flightSearchResp) throws JSONException {
        Map<Integer, List<FlightSearchResponse>> searchResults = new LinkedHashMap<>();
        List<FlightSearchResponse> oneWayFlightList = new LinkedList<>();
        JSONObject flightSearchResObj =  new JSONObject(flightSearchResp);
        JSONObject responseObj = flightSearchResObj.getJSONObject("response");
        JSONArray searchResultArray = responseObj.getJSONArray("search_results");
        if(searchResultArray.length() > 0){
            JSONArray onGoingFlightArray = searchResultArray.getJSONArray(0);   // oneWay flights
            getAllFlights(onGoingFlightArray,oneWayFlightList);
            searchResults.put(0,oneWayFlightList);
            if(searchResultArray.length() > 1){    // roundTrip flights
                List<FlightSearchResponse> returnTripFlightList = new LinkedList<>();
                JSONArray returnTripFlightArray = searchResultArray.getJSONArray(1);   // ReturnTrip flights
                getAllFlights(returnTripFlightArray,returnTripFlightList);
                searchResults.put(1,returnTripFlightList);
            }
        }
        return searchResults;
    }

    private static void getAllFlights(JSONArray flightJSONArray, List<FlightSearchResponse> flightList) throws JSONException {
        if(flightJSONArray.length() > 0)
        {
            for(int i=0 ; i < flightJSONArray.length() ; i++){
                try {
                    flightList.add(toEntity(flightJSONArray.getJSONObject(i).toString()));
                }catch(Exception e){
                    System.out.println("Getting Exception in Adding the response");
                }
            }
        }

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
        //flightSearchManager.saveFlightSearchResponse(getAllFlightDataValues(origin, destination, isRefundable, originDepartDate,
           //     originDepartTime, destinationArrivalDate, destinationArrivalTime, flightName, flightNumber,  seatRemain,
          //      pricePerAdult, pricePerChild, pricePerInfant));
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