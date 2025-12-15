package com.example.hotels.service;

import com.example.hotels.entity.Inventory;
import com.example.hotels.entity.Room;
import com.example.hotels.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        for(; !today.isAfter(endDate); today = today.plusDays(1)){
           Inventory inventory = Inventory.builder()
                   .hotel(room.getHotel())
                   .room(room)
                   .bookedCount(0)
                   .city(room.getHotel().getCity())
                   .date(today)
                   .price(room.getBasePrice())
                   .surgeFactor(BigDecimal.ONE)
                   .totalCount(room.getTotalCount())
                   .closed(false)
                   .build();

           inventoryRepository.save(inventory);

        }
    }

    @Override
    public void deleteFutureInventories(Room room) {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByDateAfterAndRoom(today, room);
    }
}