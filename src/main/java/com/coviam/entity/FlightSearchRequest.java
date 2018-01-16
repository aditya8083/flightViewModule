package com.coviam.entity;

import org.springframework.stereotype.Component;

@Component
public class FlightSearchRequest {

        private String origin;
        private String destination;
        private String originDepartDate;
        private String destinationArrivalDate;
        private int adults;
        private int infants;
        private int children;
        private String flightType;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

    public String getOriginDepartDate() {
        return originDepartDate;
    }

    public void setOriginDepartDate(String originDepartDate) {
        this.originDepartDate = originDepartDate;
    }

    public String getDestinationArrivalDate() {
        return destinationArrivalDate;
    }

    public void setDestinationArrivalDate(String destinationArrivalDate) {
        this.destinationArrivalDate = destinationArrivalDate;
    }

    public int getAdults() {
            return adults;
        }

        public void setAdults(int adults) {
            this.adults = adults;
        }

        public int getInfants() {
            return infants;
        }

        public void setInfants(int infants) {
            this.infants = infants;
        }

        public int getChildren() {
            return children;
        }

        public void setChildren(int children) {
            this.children = children;
        }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }

    @Override
    public String toString() {
        return "FlightSearchRequest{" + "origin='" + origin + '\'' + ", destination='" + destination + '\'' + ", originDepartDate='" + originDepartDate + '\'' + ", destinationArrivalDate='" + destinationArrivalDate + '\'' + ", adults=" + adults + ", infants=" + infants + ", children=" + children + ", flightType='" + flightType + '\'' + '}';
    }
}
