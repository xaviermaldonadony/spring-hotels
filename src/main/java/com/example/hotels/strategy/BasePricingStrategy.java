package com.example.hotels.strategy;

import com.example.hotels.entity.Inventory;

import java.math.BigDecimal;

//@Service
public class BasePricingStrategy implements PricingStrategy{

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
