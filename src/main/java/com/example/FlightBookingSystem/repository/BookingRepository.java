package com.example.FlightBookingSystem.repository;

import com.example.FlightBookingSystem.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Additional query methods (if needed) can be defined here
}
