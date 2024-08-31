package com.example.FlightBookingSystem.DTO;

import com.example.FlightBookingSystem.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class BookingResponseDTO {
    private Long id;
    private LocalDateTime bookingDate;
    private BigDecimal totalAmount;
    private Long flightId;
    private Long passengerId;
    private BookingStatus status;


    public BookingResponseDTO(Long id, LocalDateTime bookingDate, BigDecimal totalAmount, Long flightId, Long passengerId, BookingStatus status) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.status = status;
    }


}
