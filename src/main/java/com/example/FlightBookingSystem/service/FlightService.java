package com.example.FlightBookingSystem.service;
import com.example.FlightBookingSystem.Model.Flight;
import com.example.FlightBookingSystem.exception.FlightNotFoundException;
import com.example.FlightBookingSystem.repository.FlightRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FlightService {

        @Autowired
        private FlightRespository flightRepository;

        public Flight createFlight(Flight flight) {
            return flightRepository.save(flight);
        }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

        public Flight updateFlight(Long id, Flight flight) {
            if (!flightRepository.existsById(id)) {
                throw new FlightNotFoundException("Flight not found with id " + id);
            }
            flight.setId(id);
            return flightRepository.save(flight);
        }

        public void deleteFlight(Long id) {
            if (!flightRepository.existsById(id)) {
                throw new FlightNotFoundException("Flight not found with id " + id);
            }
            flightRepository.deleteById(id);
        }

        public Page<Flight> getFlights(Pageable pageable) {
            return flightRepository.findAll(pageable);
        }
    }

