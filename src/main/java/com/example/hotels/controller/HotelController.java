package com.example.hotels.controller;

import com.example.hotels.dto.HotelDto;
import com.example.hotels.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/hotels")
public class HotelController {
    private final HotelService hotelService;

    @PostMapping()
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto){
        log.info("Attempting to create a new hotel with name: " + hotelDto.getName());
        HotelDto hotel = hotelService.createNewHotel(hotelDto);
        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id){
        log.info("Attempting to get hotel with ID: " + id);

        HotelDto hotelDto = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotelDto);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto){
        log.info("Attempting to update hotel with ID: " + hotelId);
        HotelDto hotel = hotelService.updateHotelById(hotelId, hotelDto);

        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId){
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<Void> activateHotel(@PathVariable Long hotelId){
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

}
