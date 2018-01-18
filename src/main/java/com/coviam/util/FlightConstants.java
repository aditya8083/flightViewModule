package com.coviam.util;

import org.springframework.stereotype.Component;

@Component
public class FlightConstants {

    public int SUCCESS_CODE = 200;
    public int FAILURE_CODE = 301;
    public int EXCEPTION_CODE = 500;
    public String SUCCESS = "Success";
    public String FAILURE = "Failure";
    public String FLIGHT_SEARCH = "flightSearch";
    public String FLIGHT_DETAIL = "flightDetail";
    public String FLIGHT_CACHE_SET = "flightCache";
    public String FLIGHT_SEARCH_COLNAME = "flightSearch";
    public String NO_FLIGHT_FOUND_MSG = "No flight found for you search criteria. Please change the search Criteria.";

}
