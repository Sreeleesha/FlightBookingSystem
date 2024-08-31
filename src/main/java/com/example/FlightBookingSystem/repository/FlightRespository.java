package com.example.FlightBookingSystem.repository;

import com.example.FlightBookingSystem.Model.Flight;
import com.example.FlightBookingSystem.Model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRespository extends JpaRepository<Flight, Long>{
    Optional<Flight> findById(Long id);
}



