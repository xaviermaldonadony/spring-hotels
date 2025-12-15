package com.example.hotels.service;

import com.example.hotels.dto.RoomDto;
import com.example.hotels.entity.Hotel;
import com.example.hotels.entity.Room;
import com.example.hotels.exceptions.ResourceNotFoundException;
import com.example.hotels.repository.HotelRepository;
import com.example.hotels.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryService inventoryService;
    private final ModelMapper  modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId,RoomDto roomDto) {
        log.info("Creating a new room in hotel with ID: " + hotelId );
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        Room room  = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        if(hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel with ID: " + hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        return hotel.getRooms().stream().map(room -> modelMapper.map(room, RoomDto.class)).toList();
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting room with ID: " + roomId);

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        return modelMapper.map(room, RoomDto.class);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting room with ID: " + roomId);

        boolean exists = roomRepository.existsById(roomId);

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

//        TODO: thowing erro sa far. seems like a threading issue.
        inventoryService.deleteFutureInventories(room);
        roomRepository.deleteById(roomId);
    }
}
