package com.example.FlightBookingSystem.repository;

import com.example.FlightBookingSystem.Model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long>{
}


