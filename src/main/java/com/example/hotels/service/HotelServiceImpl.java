package com.example.hotels.service;

import com.example.hotels.dto.HotelDto;
import com.example.hotels.entity.Hotel;
import com.example.hotels.entity.Room;
import com.example.hotels.exceptions.ResourceNotFoundException;
import com.example.hotels.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto){
        log.info("Creating a new Hotel with name : {}", hotelDto.getName());

        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);

        log.info("Created a new hotel with ID: {}", hotelDto.getId());

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id){
        log.info("Getting Hotel with ID: {}", id);

        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Hotel not found with ID: " + id)));

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating Hotel with ID: {}", id);

        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        hotelRepository.deleteById(id);
        for (Room room: hotel.getRooms()){
            inventoryService.deleteFutureInventories(room);
        }
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating the hotel with ID {}", hotelId);

       Hotel hotel = hotelRepository
               .findById(hotelId)
               .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

       hotel.setActive(true);
      // Assuming its only done once
        for (Room room: hotel.getRooms()){
           inventoryService.initializeRoomForAYear(room);
        }

    }


}
