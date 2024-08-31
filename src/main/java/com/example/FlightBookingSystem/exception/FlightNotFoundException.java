package com.example.FlightBookingSystem.exception;

public class FlightNotFoundException extends RuntimeException{

    public FlightNotFoundException(String message) {
        super(message);
    }
}
