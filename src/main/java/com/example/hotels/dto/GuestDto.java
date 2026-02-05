package com.example.hotels.dto;

import com.example.hotels.entity.User;
import com.example.hotels.entity.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
