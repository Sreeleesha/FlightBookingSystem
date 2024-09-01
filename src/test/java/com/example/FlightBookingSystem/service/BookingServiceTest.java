package com.example.FlightBookingSystem.service;

import com.example.FlightBookingSystem.DTO.BookingCreateDTO;
import com.example.FlightBookingSystem.DTO.BookingResponseDTO;
import com.example.FlightBookingSystem.DTO.BookingUpdateDTO;
import com.example.FlightBookingSystem.Model.Booking;
import com.example.FlightBookingSystem.Model.Flight;
import com.example.FlightBookingSystem.Model.Passenger;
import com.example.FlightBookingSystem.enums.BookingStatus;
import com.example.FlightBookingSystem.exception.BookingNotFoundException;
import com.example.FlightBookingSystem.exception.FlightNotFoundException;
import com.example.FlightBookingSystem.exception.PassengerNotFoundException;
import com.example.FlightBookingSystem.repository.BookingRepository;
import com.example.FlightBookingSystem.repository.FlightRespository;
import com.example.FlightBookingSystem.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightRespository flightRepository;

    @Mock
    private PassengerRepository passengerRepository;

    private Flight flight;
    private Passenger passenger;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        flight = new Flight();
        flight.setId(1L);
        flight.setSeatsAvailable(10);
        flight.setPricePerSeat(BigDecimal.valueOf(100));
        flight.setDepartureTime(LocalDateTime.now().plusHours(3));

        passenger = new Passenger();
        passenger.setId(1L);

        booking = new Booking();
        booking.setId(1L);
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalAmount(flight.getPricePerSeat());
        booking.setStatus(BookingStatus.CONFIRMED);
    }

    @Test
    public void testCreateBookingSuccess() {
        BookingCreateDTO createDTO = new BookingCreateDTO();
        createDTO.setFlightId(flight.getId());
        createDTO.setPassengerId(passenger.getId());
        createDTO.setBookingDate(LocalDateTime.now());

        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight));
        when(passengerRepository.findById(anyLong())).thenReturn(Optional.of(passenger));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO responseDTO = bookingService.createBooking(createDTO);

        assertNotNull(responseDTO);
        assertEquals(booking.getId(), responseDTO.getId());
        assertEquals(booking.getBookingDate(), responseDTO.getBookingDate());
        assertEquals(booking.getTotalAmount(), responseDTO.getTotalAmount());
        assertEquals(booking.getFlight().getId(), responseDTO.getFlightId());
        assertEquals(booking.getPassenger().getId(), responseDTO.getPassengerId());
        assertEquals(booking.getStatus(), responseDTO.getStatus());
    }

    @Test
    public void testCreateBookingFlightNotFound() {
        BookingCreateDTO createDTO = new BookingCreateDTO();
        createDTO.setFlightId(flight.getId());
        createDTO.setPassengerId(passenger.getId());

        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class, () -> bookingService.createBooking(createDTO));
    }

    @Test
    public void testRetrieveBookingSuccess() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingResponseDTO responseDTO = bookingService.retrieveBooking(booking.getId());

        assertNotNull(responseDTO);
        assertEquals(booking.getId(), responseDTO.getId());
    }

    @Test
    public void testRetrieveBookingNotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.retrieveBooking(1L));
    }

    @Test
    public void testCancelBookingSuccess() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO responseDTO = bookingService.cancelBooking(booking.getId());

        assertNotNull(responseDTO);
        assertEquals(BookingStatus.CANCELLED, responseDTO.getStatus());
    }

    @Test
    public void testCancelBookingNotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.cancelBooking(1L));
    }

    @Test
    public void testUpdateBookingSuccess() {
        BookingUpdateDTO updateDTO = new BookingUpdateDTO();
        updateDTO.setStatus(BookingStatus.CANCELLED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO responseDTO = bookingService.updateBooking(booking.getId(), updateDTO);

        assertNotNull(responseDTO);
        assertEquals(BookingStatus.CANCELLED, responseDTO.getStatus());
    }

    @Test
    public void testUpdateBookingNotFound() {
        BookingUpdateDTO updateDTO = new BookingUpdateDTO();
        updateDTO.setStatus(BookingStatus.CANCELLED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.updateBooking(1L, updateDTO));
    }

    @Test
    public void testListBookingsSuccess() {
        Page<Booking> bookings = new PageImpl<>(Arrays.asList(booking));
        when(bookingRepository.findAll(any(Pageable.class))).thenReturn(bookings);

        Page<BookingResponseDTO> responsePage = bookingService.listBookings(0, 10, "bookingDate", true);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
    }
}
