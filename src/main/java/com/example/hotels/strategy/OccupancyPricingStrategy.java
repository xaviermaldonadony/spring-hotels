package com.example.hotels.strategy;

import com.example.hotels.entity.Inventory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

//@Service
@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount();

        return occupancyRate > 0.8 ? price.multiply(BigDecimal.valueOf(1.2)) : price;
    }
}
