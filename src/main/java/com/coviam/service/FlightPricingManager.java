package com.coviam.service;

import com.coviam.entity.FlightPricingRequest;
import com.coviam.entity.FlightSearchRequest;
import com.coviam.entity.FlightSearchResponse;
import com.coviam.util.RandomGenerator;
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

@Component
public class FlightPricingManager {
    private String SUCCESS = "Success";
    private String FAILURE = "Failure";
    @Autowired RandomGenerator randomGenerator;


    public String getFlightPricing( String  flightId) {
      //  FlightPricingRequest flightPricingRequest = getFlightPricingRequestParams(flightId);
        String flightPricingIds[] = flightId.split(",");
        JSONObject flightPricingRespObj = new JSONObject();
        JSONArray flightResArr = new JSONArray();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        getFlightPriceResponse(flightPricingIds, session, flightResArr);
        flightPricingRespObj.put("result", flightResArr);
        flightPricingRespObj.put("flightPricingID", randomGenerator.generateRandomString());
        if(flightPricingRespObj.getJSONArray("result").length()!=0){
            flightPricingRespObj.put("resCode", "200");
            flightPricingRespObj.put("resMessage", SUCCESS);
            System.out.println("Flight price response got successfully");
            return flightPricingRespObj.toString().replaceAll("\\\\", "");
        }
        flightPricingRespObj.put("resCode", "301");
        flightPricingRespObj.put("resMessage", FAILURE);
        return flightPricingRespObj.toString().replaceAll("\\\\", "");
    }

    private void getFlightPriceResponse(String[] flightPricingIds, Session session, JSONArray flightResArr) {
      //  String onGoingTripFlightId = flightPricingIds[0];
        String onGoingTripFlightId = "1";
        Criteria criteria1 = session.createCriteria(FlightSearchResponse.class)
                .add(Restrictions.eq("id", Integer.parseInt(onGoingTripFlightId)));
        List<FlightSearchResponse> OnGoingTripResponseList = (List<FlightSearchResponse>)criteria1.list();
        flightResArr.put(new JSONObject(OnGoingTripResponseList.get(0)));
        if(flightPricingIds.length > 1){
          //  String returnTripFlightId = flightPricingIds[1];
              String returnTripFlightId = "2";
            Criteria  criteria2 = session.createCriteria(FlightSearchResponse.class)
                    .add(Restrictions.eq("id",  Integer.parseInt(returnTripFlightId)));
            List<FlightSearchResponse> returnTripResponseList = (List<FlightSearchResponse>)criteria2.list();
            flightResArr.put(new JSONObject(returnTripResponseList.get(0)));
        }
        System.out.println("No of Flights " + flightResArr.length());
    }

    private FlightPricingRequest getFlightPricingRequestParams(HttpServletRequest request) {
        FlightPricingRequest flightPricingRequest = new FlightPricingRequest();
        flightPricingRequest.setId((request.getParameter("id")));
        return  flightPricingRequest;

    }

}
