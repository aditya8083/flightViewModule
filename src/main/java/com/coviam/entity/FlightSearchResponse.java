package com.coviam.entity;

import javax.persistence.*;

@Entity
public class FlightSearchResponse {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id")
    @SequenceGenerator(name = "id", sequenceName = "id", allocationSize = 1, initialValue = 50)
    private int id;
    private String origin;
    private String destination;
    private boolean isRefundable;
    private String originDepartDate;
    private String originDepartTime;
    private String destinationArrivalDate;
    private String destinationArrivalTime;
    private String flightName;
    private String flightNumber;
    private String seatRemain;
    private String pricePerAdult;
    private String pricePerChild;
    private String pricePerInfant;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }

    public String getOriginDepartDate() {
        return originDepartDate;
    }

    public void setOriginDepartDate(String originDepartDate) {
        this.originDepartDate = originDepartDate;
    }

    public String getOriginDepartTime() {
        return originDepartTime;
    }

    public void setOriginDepartTime(String originDepartTime) {
        this.originDepartTime = originDepartTime;
    }

    public String getDestinationArrivalDate() {
        return destinationArrivalDate;
    }

    public void setDestinationArrivalDate(String destinationArrivalDate) {
        this.destinationArrivalDate = destinationArrivalDate;
    }

    public String getDestinationArrivalTime() {
        return destinationArrivalTime;
    }

    public void setDestinationArrivalTime(String destinationArrivalTime) {
        this.destinationArrivalTime = destinationArrivalTime;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getSeatRemain() {
        return seatRemain;
    }

    public void setSeatRemain(String seatRemain) {
        this.seatRemain = seatRemain;
    }

    public String getPricePerAdult() {
        return pricePerAdult;
    }

    public void setPricePerAdult(String pricePerAdult) {
        this.pricePerAdult = pricePerAdult;
    }

    public String getPricePerChild() {
        return pricePerChild;
    }

    public void setPricePerChild(String pricePerChild) {
        this.pricePerChild = pricePerChild;
    }

    public String getPricePerInfant() {
        return pricePerInfant;
    }

    public void setPricePerInfant(String pricePerInfant) {
        this.pricePerInfant = pricePerInfant;
    }

    @Override
    public String toString() {
        return "FlightSearchResponse{" + "origin='" + origin + '\'' + ", destination='" + destination + '\'' + ", isRefundable=" + isRefundable + ", originDepartDate='" + originDepartDate + '\'' + ", originDepartTime='" + originDepartTime + '\'' + ", destinationArrivalDate='" + destinationArrivalDate + '\'' + ", destinationArrivalTime='" + destinationArrivalTime + '\'' + ", flightName='" + flightName + '\'' + ", flightNumber='" + flightNumber + '\'' + ", seatRemain='" + seatRemain + '\'' + ", pricePerAdult='" + pricePerAdult + '\'' + ", pricePerChild='" + pricePerChild + '\'' + ", pricePerInfant='" + pricePerInfant + '\'' + '}';
    }
}
