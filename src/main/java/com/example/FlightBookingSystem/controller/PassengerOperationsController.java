package com.example.FlightBookingSystem.controller;
import com.example.FlightBookingSystem.service.PassengerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.FlightBookingSystem.Model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/passengers")
public class PassengerOperationsController {

        @Autowired
        private PassengerService passengerService;

        @PostMapping
        public ResponseEntity<Passenger> createPassenger(@RequestBody Passenger passenger) {
            Passenger createdPassenger = passengerService.createPassenger(passenger);
            return ResponseEntity.status(201).body(createdPassenger);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Passenger> getPassenger(@PathVariable Long id) {
            return passengerService.getPassengerById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<Passenger> updatePassenger(@PathVariable Long id, @RequestBody Passenger passenger) {
            return ResponseEntity.ok(passengerService.updatePassenger(id, passenger));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
            passengerService.deletePassenger(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping
        public ResponseEntity<Page<Passenger>> getPassengers(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "id,asc") String sort) {

            Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")[0])
                    .ascending()); // Change to descending() if needed
            Page<Passenger> passengers = passengerService.getPassengers(pageable);
            return ResponseEntity.ok(passengers);
        }
    }

