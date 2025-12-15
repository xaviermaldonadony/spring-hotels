package com.example.hotels.repository;

import com.example.hotels.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
