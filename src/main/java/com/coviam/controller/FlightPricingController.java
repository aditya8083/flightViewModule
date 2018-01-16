package com.coviam.controller;


import com.coviam.entity.FlightSearchResponse;
import com.coviam.service.FlightPricingManager;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FlightPricingController {

    @Autowired FlightPricingManager flightPricingManager;

    @RequestMapping(value ="/flight/price", method = RequestMethod.POST)
    public ModelAndView flightPricing(@ModelAttribute("flightSearchResponse") FlightSearchResponse flightSearchResponse){
        System.out.println("Getting Flight Pricing");
        System.out.println("====================" + flightSearchResponse.getId() + "=========================");
        String flightDetailResponse = flightPricingManager.getFlightPricing(String.valueOf(flightSearchResponse.getId()));
        JSONObject pricingResponseObj = new JSONObject(flightDetailResponse);
        JSONArray flightDetailResponseArray = new JSONArray();
        if(pricingResponseObj.getJSONArray("result").getJSONArray(0).length()!=0){

            JSONArray flightPriceResultArray = pricingResponseObj.getJSONArray("result");
            for(int i =0 ;i < flightPriceResultArray.length(); i++){
                flightDetailResponseArray.put(i, flightPriceResultArray.getJSONArray(i).getJSONObject(0));
                System.out.println(flightPriceResultArray.getJSONArray(i).getJSONObject(0).toString());
            }
        }
        List<String> list=new ArrayList<String>();
        //list.add(new FlightSearchResponse("rahul");
        list.add("aditya");
        list.add("sachin");
        return new ModelAndView("flightPriceResponse","list",list);
    }
}
