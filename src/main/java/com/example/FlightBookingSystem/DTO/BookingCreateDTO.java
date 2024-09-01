package com.example.FlightBookingSystem.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BookingCreateDTO {
    private Long flightId;
    private Long passengerId;
    private LocalDateTime bookingDate;


}
