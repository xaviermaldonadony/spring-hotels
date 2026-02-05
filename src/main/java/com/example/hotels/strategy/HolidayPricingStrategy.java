package com.example.hotels.strategy;

import com.example.hotels.entity.Inventory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

//@Service
@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy {
    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);

        boolean isTodayHoliday = true;// TO DO

        if(isTodayHoliday){
            return price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
