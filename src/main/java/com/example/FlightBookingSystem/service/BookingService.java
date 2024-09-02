package com.example.FlightBookingSystem.service;
import com.example.FlightBookingSystem.Model.Booking;
import com.example.FlightBookingSystem.Model.Flight;
import com.example.FlightBookingSystem.Model.Passenger;
import com.example.FlightBookingSystem.DTO.BookingCreateDTO;
import com.example.FlightBookingSystem.DTO.BookingResponseDTO;
import com.example.FlightBookingSystem.DTO.BookingUpdateDTO;
import com.example.FlightBookingSystem.enums.BookingStatus;

import com.example.FlightBookingSystem.exception.BookingNotFoundException;
import com.example.FlightBookingSystem.exception.FlightNotFoundException;
import com.example.FlightBookingSystem.exception.PassengerNotFoundException;
import com.example.FlightBookingSystem.repository.BookingRepository;
import com.example.FlightBookingSystem.repository.FlightRespository;
import com.example.FlightBookingSystem.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRespository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public BookingResponseDTO createBooking(BookingCreateDTO createDTO) {
        Flight flight = flightRepository.findById(createDTO.getFlightId())
                .orElseThrow(() -> new com.example.FlightBookingSystem.exception.FlightNotFoundException("Flight not found with id " + createDTO.getFlightId()));
        Passenger passenger = passengerRepository.findById(createDTO.getPassengerId())
                .orElseThrow(() -> new com.example.FlightBookingSystem.exception.PassengerNotFoundException("Passenger not found with id " + createDTO.getPassengerId()));

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        booking.setBookingDate(createDTO.getBookingDate());
        booking.setTotalAmount(flight.getPricePerSeat()); // Dynamically calculate total amount
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);
        kafkaTemplate.send(TOPIC, "Booking Created", savedBooking);
        return new BookingResponseDTO(
                savedBooking.getId(),
                savedBooking.getBookingDate(),
                savedBooking.getTotalAmount(),
                savedBooking.getFlight().getId(),
                savedBooking.getPassenger().getId(),
                savedBooking.getStatus()
        );
    }

    public BookingResponseDTO retrieveBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new com.example.FlightBookingSystem.exception.BookingNotFoundException("Booking not found with id " + id));

        return new BookingResponseDTO(
                booking.getId(),
                booking.getBookingDate(),
                booking.getTotalAmount(),
                booking.getFlight().getId(),
                booking.getPassenger().getId(),
                booking.getStatus()
        );
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new com.example.FlightBookingSystem.exception.BookingNotFoundException("Booking not found with id " + id));

        booking.setStatus(BookingStatus.CANCELLED);

        Booking updatedBooking = bookingRepository.save(booking);
        kafkaTemplate.send(TOPIC, "Booking Cancelled", updatedBooking);
        return new BookingResponseDTO(
                updatedBooking.getId(),
                updatedBooking.getBookingDate(),
                updatedBooking.getTotalAmount(),
                updatedBooking.getFlight().getId(),
                updatedBooking.getPassenger().getId(),
                updatedBooking.getStatus()
        );
    }

    @Transactional
    public BookingResponseDTO updateBooking(Long id, BookingUpdateDTO updateDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new com.example.FlightBookingSystem.exception.BookingNotFoundException("Booking not found with id " + id));

        if (updateDTO.getBookingDate() != null) {
            booking.setBookingDate(updateDTO.getBookingDate());
        }
        if (updateDTO.getTotalAmount() != null) {
            booking.setTotalAmount(updateDTO.getTotalAmount());
        }
        if (updateDTO.getStatus() != null) {
            booking.setStatus(updateDTO.getStatus());
        }

        Booking updatedBooking = bookingRepository.save(booking);
        kafkaTemplate.send(TOPIC, "Booking Updated", updatedBooking);
        return new BookingResponseDTO(
                updatedBooking.getId(),
                updatedBooking.getBookingDate(),
                updatedBooking.getTotalAmount(),
                updatedBooking.getFlight().getId(),
                updatedBooking.getPassenger().getId(),
                updatedBooking.getStatus()
        );
    }

    public Page<BookingResponseDTO> listBookings(int page, int size, String sortBy, boolean ascending) {
        Pageable pageable = PageRequest.of(page, size, ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<Booking> bookingPage = bookingRepository.findAll(pageable);

        return bookingPage.map(booking -> new BookingResponseDTO(
                booking.getId(),
                booking.getBookingDate(),
                booking.getTotalAmount(),
                booking.getFlight().getId(),
                booking.getPassenger().getId(),
                booking.getStatus()
        ));
    }


}
