package com.coviam.service;

import com.coviam.entity.FlightPricingRequest;
import com.coviam.entity.FlightSearchResponse;
import com.coviam.util.EscapeCharacter;
import com.coviam.util.FlightConstants;
import com.coviam.util.RandomGenerator;
import com.coviam.util.ResponseEntity;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FlightPricingManager {
    @Autowired
    RandomGenerator randomGenerator;
    @Autowired
    FlightConstants flightConstants;
    @Autowired
    EscapeCharacter escapeCharacter;


    public String getFlightPricing(HttpServletRequest request) {
        FlightPricingRequest flightPricingRequest = getFlightPricingRequestParams(request);
        String flightPricingIds[] = flightPricingRequest.getId().split(",");
        JSONObject flightPricingRespObj = new JSONObject();
        JSONArray flightResArr = new JSONArray();
        String response = " ";
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            getFlightPriceResponse(flightPricingIds, session, flightResArr);
            flightPricingRespObj.put("details_result", flightResArr);
            if (flightPricingRespObj.getJSONArray("details_result").getJSONArray(0).length() != 0) {
                System.out.println("Flight Detail response got successfully");
                response = new JSONObject(new ResponseEntity(flightConstants.SUCCESS_CODE, flightConstants.SUCCESS, randomGenerator.generateRandomString(),
                           flightConstants.FLIGHT_DETAIL, flightPricingRespObj)).toString();
            }else {
                response = new JSONObject(new ResponseEntity(flightConstants.FAILURE_CODE, flightConstants.FAILURE, randomGenerator.generateRandomString(),
                           flightConstants.FLIGHT_DETAIL, flightPricingRespObj)).toString();
                System.out.println("Error in Getting Flight Details");
            }
        }catch(Exception e){
            System.out.println("Exception in Getting Flight Details");
            return escapeCharacter.escapeCharacter(new JSONObject(new ResponseEntity(flightConstants.EXCEPTION_CODE, flightConstants.FAILURE, randomGenerator.generateRandomString(),
                       flightConstants.FLIGHT_DETAIL,new JSONObject())).toString());
        }
        return escapeCharacter.escapeCharacter(response);
    }

    private void getFlightPriceResponse(String[] flightPricingIds, Session session, JSONArray flightResArr) {
        String onGoingTripFlightId = flightPricingIds[0];
        Criteria criteria1 = session.createCriteria(FlightSearchResponse.class)
                .add(Restrictions.eq("id", Integer.parseInt(onGoingTripFlightId)));
        List<FlightSearchResponse> OnGoingTripResponseList = (List<FlightSearchResponse>)criteria1.list();
        flightResArr.put( OnGoingTripResponseList);
        if(flightPricingIds.length > 1){
            String returnTripFlightId = flightPricingIds[1];
            Criteria criteria2 = session.createCriteria(FlightSearchResponse.class)
                    .add(Restrictions.eq("id",  Integer.parseInt(returnTripFlightId)));
            List<FlightSearchResponse> returnTripResponseList = (List<FlightSearchResponse>)criteria2.list();
            flightResArr.put(returnTripResponseList);
        }
    }

    private FlightPricingRequest getFlightPricingRequestParams(HttpServletRequest request) {
        FlightPricingRequest flightPricingRequest = new FlightPricingRequest();
        flightPricingRequest.setId((request.getParameter("id")));
        return  flightPricingRequest;

    }

}
