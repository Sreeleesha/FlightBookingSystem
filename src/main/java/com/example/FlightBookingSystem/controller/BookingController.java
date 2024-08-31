package com.example.FlightBookingSystem.controller;

import com.example.FlightBookingSystem.DTO.BookingCreateDTO;
import com.example.FlightBookingSystem.DTO.BookingResponseDTO;
import com.example.FlightBookingSystem.DTO.BookingUpdateDTO;


import com.example.FlightBookingSystem.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public BookingResponseDTO createBooking(@RequestBody BookingCreateDTO createDTO) {
        return bookingService.createBooking(createDTO);
    }

    @GetMapping("/{id}")
    public BookingResponseDTO getBooking(@PathVariable Long id) {
        return bookingService.retrieveBooking(id);
    }

    @PutMapping("/{id}/cancel")
    public BookingResponseDTO cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    @PutMapping("/{id}")
    public BookingResponseDTO updateBooking(
            @PathVariable Long id,
            @RequestBody BookingUpdateDTO updateDTO) {
        return bookingService.updateBooking(id, updateDTO);
    }

    @GetMapping
    public Page<BookingResponseDTO> listBookings(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        return bookingService.listBookings(page, size, sortBy, ascending);
    }
}
