package com.example.hotels.repository;

import com.example.hotels.entity.Inventory;
import com.example.hotels.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteByDateAfterAndRoom(LocalDate date, Room room);
}
