package com.example.hotels.controller;

import com.example.hotels.dto.BookingDto;
import com.example.hotels.dto.BookingRequest;
import com.example.hotels.dto.GuestDto;
import com.example.hotels.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService hotelBookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(hotelBookingService.initializeBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId,
                                                @RequestBody List<GuestDto> guestDtoList ){
        return ResponseEntity.ok(hotelBookingService.addBuests(bookingId, guestDtoList));
    }

}
