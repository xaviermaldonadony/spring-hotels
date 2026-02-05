package com.example.hotels.service;

import com.example.hotels.dto.BookingDto;
import com.example.hotels.dto.BookingRequest;
import com.example.hotels.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto initializeBooking(BookingRequest bookingRequest);

    BookingDto addBuests(Long bookingId, List<GuestDto> guestDtoList);
}
