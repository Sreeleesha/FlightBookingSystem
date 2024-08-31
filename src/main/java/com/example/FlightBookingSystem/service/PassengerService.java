package com.example.FlightBookingSystem.service;
import com.example.FlightBookingSystem.Model.Passenger;
import com.example.FlightBookingSystem.exception.PassengerNotFoundException;
import com.example.FlightBookingSystem.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PassengerService {

        @Autowired
        private PassengerRepository passengerRepository;

        public Passenger createPassenger(Passenger passenger) {
            return passengerRepository.save(passenger);
        }

        public Optional<Passenger> getPassengerById(Long id) {
            return passengerRepository.findById(id);
        }

        public Passenger updatePassenger(Long id, Passenger passenger) {
            if (!passengerRepository.existsById(id)) {
                throw new PassengerNotFoundException("Passenger not found with id " + id);
            }
            passenger.setId(id);
            return passengerRepository.save(passenger);
        }

        public void deletePassenger(Long id) {
            if (!passengerRepository.existsById(id)) {
                throw new PassengerNotFoundException("Passenger not found with id " + id);
            }
            passengerRepository.deleteById(id);
        }

        public Page<Passenger> getPassengers(Pageable pageable) {
            return passengerRepository.findAll(pageable);
        }
    }


