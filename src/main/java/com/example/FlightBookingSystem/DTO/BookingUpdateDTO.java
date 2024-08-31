package com.example.FlightBookingSystem.DTO;

import com.example.FlightBookingSystem.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class BookingUpdateDTO {
    private LocalDateTime bookingDate;
    private BigDecimal totalAmount;
    private BookingStatus status;

    // Getters and Setters
}
