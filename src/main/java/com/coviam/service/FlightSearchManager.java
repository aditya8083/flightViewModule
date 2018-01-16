package com.coviam.service;

import com.coviam.cache.CachingWrapper;
import com.coviam.controller.FlightSearchController;
import com.coviam.entity.FlightSearchRequest;
import com.coviam.entity.FlightSearchResponse;
import com.coviam.util.RandomGenerator;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Service
public class FlightSearchManager {

    private String FLIGHT_CACHE_SET = "flightCache";
    private String FLIGHT_SEARCH_COLNAME = "flightSearch";
    private String SUCCESS = "Success";
    private String FAILURE = "Failure";

   @Autowired CachingWrapper cachingWrapper;
   @Autowired RandomGenerator randomGenerator;

    public String getAllFlights(HttpServletRequest request) throws JSONException {
        FlightSearchRequest flightSearchRequest = getFlightSearchRequestParams(request);
        System.out.println(flightSearchRequest.toString());
        if(flightSearchResponsePresentInCache(flightSearchRequest)){
            return cachingWrapper.readValue(FLIGHT_CACHE_SET,flightSearchRequest.toString(), FLIGHT_SEARCH_COLNAME);
        }
        JSONObject flightSearchRespObj = new JSONObject();
        JSONArray flightResArr = new JSONArray();
        List<FlightSearchResponse> flightSearchResponseOneWayList = new ArrayList<FlightSearchResponse>();
        int totalPaxCount = getPaxCount(flightSearchRequest);

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();

        flightSearchResponseOneWayList = getAllOneWayFlights(flightSearchRequest,session);
        flightResArr.put(0,flightSearchResponseOneWayList);

        if(flightSearchRequest.getFlightType().equalsIgnoreCase("ROUNDTRIP")){
            List<FlightSearchResponse> flightSearchResponseReturnTripList = new ArrayList<FlightSearchResponse>();
            flightSearchResponseReturnTripList = getAllReturnTripFlights(flightSearchRequest,session);
            flightResArr.put(1,flightSearchResponseReturnTripList);
        }
        session.close();
        System.out.println("Reached here");
        flightSearchRespObj.put("result", flightResArr);
        flightSearchRespObj.put("flightSearchID", randomGenerator.generateRandomString());
        if(flightSearchRespObj.getJSONArray("result").getJSONArray(0).length() !=0 ){
            flightSearchRespObj.put("resCode", "200");
            flightSearchRespObj.put("resMessage", SUCCESS);
            cachingWrapper.writeWithoutCompression(FLIGHT_CACHE_SET, flightSearchRequest.toString(),FLIGHT_SEARCH_COLNAME, flightSearchRespObj.toString());
            System.out.println("Flight Search response got successfully");
            return flightSearchRespObj.toString().replaceAll("\\\\", "");
        }
        flightSearchRespObj.put("resCode", "301");
        flightSearchRespObj.put("resMessage", FAILURE);
        return flightSearchRespObj.toString().replaceAll("\\\\", "");
    }

    private boolean flightSearchResponsePresentInCache(FlightSearchRequest flightSearchRequest) {
        if(!StringUtils.isBlank(cachingWrapper.readValue(FLIGHT_CACHE_SET, flightSearchRequest.toString(), FLIGHT_SEARCH_COLNAME))){
            return true;
        }
        return false;
    }


    private List<FlightSearchResponse> getAllReturnTripFlights(FlightSearchRequest flightSearchRequest, Session session) {
        session.beginTransaction();
      //  Query query = session.createQuery("from FlightSearchResponse ");
        Criteria criteria = session.createCriteria(FlightSearchResponse.class)
                            .add(Restrictions.eq("origin", flightSearchRequest.getDestination()))
                            .add(Restrictions.eq("destination", flightSearchRequest.getOrigin()))
                            .add(Restrictions.eq("originDepartDate", flightSearchRequest.getDestinationArrivalDate()));

        List<FlightSearchResponse> returnTripResponseList = (List<FlightSearchResponse>)criteria.list();
        System.out.println( "No of Flights in return Trip : " + returnTripResponseList.size());
        session.getTransaction().commit();
        return returnTripResponseList;

    }

    private List<FlightSearchResponse> getAllOneWayFlights(FlightSearchRequest flightSearchRequest, Session session) {
        session.beginTransaction();
      //  Query query = session.createQuery("from FlightSearchResponse ");
      //  List<FlightSearchResponse> oneWayResponseList = (List<FlightSearchResponse>)query.list();
       Criteria criteria = session.createCriteria(FlightSearchResponse.class)
                          .add(Restrictions.eq("origin", flightSearchRequest.getOrigin()).ignoreCase())
                          .add(Restrictions.eq("destination", flightSearchRequest.getDestination()).ignoreCase())
                         .add(Restrictions.eq("originDepartDate", flightSearchRequest.getOriginDepartDate()));


        List<FlightSearchResponse> oneWayResponseList = (List<FlightSearchResponse>)criteria.list();
        System.out.println( "No of Flights in OneWay : " + oneWayResponseList.size());
        session.getTransaction().commit();
        return oneWayResponseList;
    }

    private int getPaxCount(FlightSearchRequest flightSearchRequest) {
      return flightSearchRequest.getAdults() + flightSearchRequest.getChildren() + flightSearchRequest.getInfants();
      
    }
    private FlightSearchRequest getFlightSearchRequestParams(HttpServletRequest request) {
        FlightSearchRequest flightSearchRequest = new FlightSearchRequest();
        flightSearchRequest.setOrigin(request.getParameter("origin"));
        flightSearchRequest.setDestination(request.getParameter("destination"));
        flightSearchRequest.setOriginDepartDate(request.getParameter("originDepartDate"));
        flightSearchRequest.setDestinationArrivalDate(request.getParameter("destinationArrivalDate"));
        flightSearchRequest.setAdults(Integer.parseInt(request.getParameter("adults")));
        flightSearchRequest.setChildren(!request.getParameter("infants").isEmpty() ? Integer.parseInt(request.getParameter("infants")) : 0);
        flightSearchRequest.setInfants(!request.getParameter("children").isEmpty() ? Integer.parseInt(request.getParameter("children")) : 0);
        flightSearchRequest.setFlightType(request.getParameter("flightType"));
        return flightSearchRequest;
    }

    private String getFormatFlightDate(String parameter) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        return formatDate.format(cal.getTime());
    }

    public void saveFlightSearchResponse(FlightSearchResponse flightSearchResponse) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(flightSearchResponse);
        session.getTransaction().commit();
        session.close();
    }

       /* HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("apikey", flightSearchRequest.getapikey());
       // httpHeaders.setContentType(MediaType.);
     //   httpHeaders.add("Accept-Encoding", "gzip");
   //     httpHeaders.add("Accept-Language", "en-GB");
        RestTemplate restTemplate = new RestTemplate();
        final String uri = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search";
     //   String flightResponse = restTemplate.getForObject(uri, String.class, flightSearchRequest);

        HttpEntity<FlightSearchRequest> requestEntity = new HttpEntity<FlightSearchRequest>(flightSearchRequest, httpHeaders);
        ResponseEntity<String> flightResponse = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        System.out.println(flightResponse.getBody());*/


}