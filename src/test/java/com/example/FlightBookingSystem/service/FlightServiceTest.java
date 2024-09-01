package com.example.FlightBookingSystem.service;

import com.example.FlightBookingSystem.Model.Flight;
import com.example.FlightBookingSystem.exception.FlightNotFoundException;
import com.example.FlightBookingSystem.repository.FlightRespository;
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

class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRespository flightRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFlight_success() {
        Flight flight = new Flight();
        flight.setId(1L);

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        Flight result = flightService.createFlight(flight);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getFlightById_success() {
        Flight flight = new Flight();
        flight.setId(1L);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Optional<Flight> result = flightService.getFlightById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getFlightById_notFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Flight> result = flightService.getFlightById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void updateFlight_success() {
        Flight flight = new Flight();
        flight.setId(1L);

        when(flightRepository.existsById(1L)).thenReturn(true);
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        Flight result = flightService.updateFlight(1L, flight);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void updateFlight_notFound() {
        Flight flight = new Flight();
        flight.setId(1L);

        when(flightRepository.existsById(1L)).thenReturn(false);

        assertThrows(FlightNotFoundException.class, () -> flightService.updateFlight(1L, flight));
    }

    @Test
    void deleteFlight_success() {
        when(flightRepository.existsById(1L)).thenReturn(true);

        flightService.deleteFlight(1L);

        verify(flightRepository).deleteById(1L);
    }

    @Test
    void deleteFlight_notFound() {
        when(flightRepository.existsById(1L)).thenReturn(false);

        assertThrows(FlightNotFoundException.class, () -> flightService.deleteFlight(1L));
    }

    @Test
    void getFlights_success() {
        Flight flight = new Flight();
        flight.setId(1L);

        Page<Flight> flightPage = new PageImpl<>(Collections.singletonList(flight), PageRequest.of(0, 10), 1);

        when(flightRepository.findAll(any(Pageable.class))).thenReturn(flightPage);

        Page<Flight> result = flightService.getFlights(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
