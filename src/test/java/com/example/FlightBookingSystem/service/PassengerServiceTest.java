package com.example.FlightBookingSystem.service;

import com.example.FlightBookingSystem.Model.Passenger;
import com.example.FlightBookingSystem.exception.PassengerNotFoundException;
import com.example.FlightBookingSystem.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PassengerServiceTest {

    @InjectMocks
    private PassengerService passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPassenger_success() {
        // Arrange
        Passenger passenger = new Passenger();
        passenger.setId(1L);

        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        // Act
        Passenger result = passengerService.createPassenger(passenger);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getPassengerById_success() {
        // Arrange
        Passenger passenger = new Passenger();
        passenger.setId(1L);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        // Act
        Optional<Passenger> result = passengerService.getPassengerById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getPassengerById_notFound() {
        // Arrange
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Passenger> result = passengerService.getPassengerById(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updatePassenger_success() {
        // Arrange
        Passenger passenger = new Passenger();
        passenger.setId(1L);

        when(passengerRepository.existsById(1L)).thenReturn(true);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        // Act
        Passenger result = passengerService.updatePassenger(1L, passenger);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void updatePassenger_notFound() {
        // Arrange
        Passenger passenger = new Passenger();
        passenger.setId(1L);

        when(passengerRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(PassengerNotFoundException.class, () -> passengerService.updatePassenger(1L, passenger));
    }

    @Test
    void deletePassenger_success() {
        // Arrange
        when(passengerRepository.existsById(1L)).thenReturn(true);

        // Act
        passengerService.deletePassenger(1L);

        // Assert
        verify(passengerRepository).deleteById(1L);
    }

    @Test
    void deletePassenger_notFound() {
        // Arrange
        when(passengerRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(PassengerNotFoundException.class, () -> passengerService.deletePassenger(1L));
    }

    @Test
    void getPassengers_success() {
        // Arrange
        Passenger passenger = new Passenger();
        passenger.setId(1L);

        Page<Passenger> passengerPage = new PageImpl<>(Collections.singletonList(passenger), PageRequest.of(0, 10), 1);

        when(passengerRepository.findAll(any(Pageable.class))).thenReturn(passengerPage);

        // Act
        Page<Passenger> result = passengerService.getPassengers(PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
