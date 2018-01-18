package com.coviam.service;

import com.coviam.cache.CachingWrapper;
import com.coviam.entity.FlightSearchRequest;
import com.coviam.entity.FlightSearchResponse;
import com.coviam.util.EscapeCharacter;
import com.coviam.util.FlightConstants;
import com.coviam.util.RandomGenerator;
import com.coviam.util.ResponseEntity;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class FlightSearchManager {


   @Autowired
   CachingWrapper cachingWrapper;
   @Autowired
   RandomGenerator randomGenerator;
    @Autowired
    FlightConstants flightConstants;
    @Autowired
    EscapeCharacter escapeCharacter;

    public String getAllFlights(String origin, String destination, String originDepartDate, String destinationArrivalDate, String adults, String infants, String children, String flightType){
        FlightSearchRequest flightSearchRequest = getFlightSearchRequestParams(origin, destination, originDepartDate, destinationArrivalDate, adults, infants, children, flightType);
        System.out.println(flightSearchRequest.toString());
        if(flightSearchResponsePresentInCache(flightSearchRequest)){
            return cachingWrapper.readValue(flightConstants.FLIGHT_CACHE_SET,flightSearchRequest.toString(), flightConstants.FLIGHT_SEARCH_COLNAME);
        }
        JSONObject flightSearchRespObj = new JSONObject();
        JSONArray flightResArr = new JSONArray();
        List<FlightSearchResponse> flightSearchResponseOneWayList = new ArrayList<>();
        int totalPaxCount = getPaxCount(flightSearchRequest);
        String response =" ";
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();

            flightSearchResponseOneWayList = getAllOneWayFlights(flightSearchRequest, session);
            flightResArr.put(flightSearchResponseOneWayList);


            if (flightSearchRequest.getFlightType().equalsIgnoreCase("ROUNDTRIP")) {
                List<FlightSearchResponse> flightSearchResponseReturnTripList = new ArrayList<FlightSearchResponse>();
                flightSearchResponseReturnTripList = getAllReturnTripFlights(flightSearchRequest, session);
                flightResArr.put(flightSearchResponseReturnTripList);
            }
            session.close();
            System.out.println("Reached here");
            flightSearchRespObj.put("search_results", flightResArr);
            if (flightSearchRespObj.getJSONArray("search_results").getJSONArray(0).length() != 0) {
                System.out.println("Flight Search response got successfully");
                response = new JSONObject(new ResponseEntity(flightConstants.SUCCESS_CODE, flightConstants.SUCCESS, randomGenerator.generateRandomString(),
                        flightConstants.FLIGHT_SEARCH, flightSearchRespObj)).toString();
                cachingWrapper.writeWithoutCompression(flightConstants.FLIGHT_CACHE_SET, flightSearchRequest.toString(),flightConstants.FLIGHT_SEARCH_COLNAME, escapeCharacter.escapeCharacter(response));
            }else {
                response = new JSONObject(new ResponseEntity(flightConstants.FAILURE_CODE, flightConstants.NO_FLIGHT_FOUND_MSG, randomGenerator.generateRandomString(),
                        flightConstants.FLIGHT_SEARCH, new JSONObject())).toString();
                System.out.println("Error in Getting Flight Search results");
            }
        }catch(Exception e){
            System.out.println("Exception in Getting Flight Search Details");
            return escapeCharacter.escapeCharacter(new JSONObject(new ResponseEntity(flightConstants.EXCEPTION_CODE, flightConstants.FAILURE, randomGenerator.generateRandomString(),
                    flightConstants.FLIGHT_SEARCH,new JSONObject())).toString());
        }
        return escapeCharacter.escapeCharacter(response);
    }

    private boolean flightSearchResponsePresentInCache(FlightSearchRequest flightSearchRequest) {
        if(!StringUtils.isBlank(cachingWrapper.readValue(flightConstants.FLIGHT_CACHE_SET, flightSearchRequest.toString(), flightConstants.FLIGHT_SEARCH_COLNAME))){
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
    private FlightSearchRequest getFlightSearchRequestParams(String origin, String destination, String originDepartDate, String destinationArrivalDate, String adults, String infants, String children, String flightType) {
        FlightSearchRequest flightSearchRequest = new FlightSearchRequest();
        flightSearchRequest.setOrigin(origin);
        flightSearchRequest.setDestination(destination);
        flightSearchRequest.setOriginDepartDate(originDepartDate);
        flightSearchRequest.setDestinationArrivalDate(destinationArrivalDate);
        flightSearchRequest.setAdults(Integer.parseInt(adults));
        flightSearchRequest.setChildren(!infants.isEmpty() ? Integer.parseInt(infants) : 0);
        flightSearchRequest.setInfants(!children.isEmpty() ? Integer.parseInt(children) : 0);
        flightSearchRequest.setFlightType(flightType);
        return flightSearchRequest;
    }

    private String getFormatFlightDate(String parameter) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        return formatDate.format(cal.getTime());
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