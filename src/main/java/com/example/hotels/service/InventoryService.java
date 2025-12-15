package com.example.hotels.service;

import com.example.hotels.entity.Room;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteFutureInventories(Room room);
}
