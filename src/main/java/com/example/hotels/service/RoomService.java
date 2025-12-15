package com.example.hotels.service;

import com.example.hotels.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createNewRoom(Long hotelId, RoomDto roomtDto);

    List<RoomDto> getAllRoomsInHotel(Long hotelId);

    RoomDto getRoomById(Long roomId);


    void deleteRoomById(Long roomId);
}
