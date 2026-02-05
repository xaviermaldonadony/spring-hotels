package com.example.hotels.service;

import com.example.hotels.dto.HotelPriceDto;
import com.example.hotels.dto.HotelSearchRequest;
import com.example.hotels.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

}
