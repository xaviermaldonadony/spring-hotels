package com.example.hotels.service;

import com.example.hotels.entity.Hotel;
import com.example.hotels.entity.HotelMinPrice;
import com.example.hotels.entity.Inventory;
import com.example.hotels.repository.HotelMinPriceRepository;
import com.example.hotels.repository.HotelRepository;
import com.example.hotels.repository.InventoryRepository;
import com.example.hotels.strategy.PricingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class PricingUpdateService {

    // Scheduele to update the inventory and HotelMinPrice tables every hour

    private final HotelRepository hotelRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;

    // Every hour
    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(cron = "*/5 * * * * *")
    public void updatePrices(){

        int page = 0;
        int batchSize = 100;

        while (true){
           Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
           if (hotelPage.isEmpty()){
              break;
           }
           hotelPage.getContent().forEach(this::updateHotelPrices);

           page++;
        }
    }

    private void updateHotelPrices(Hotel hotel) {
        log.info("Updating hotel prices for hotel ID: {}", hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);

        updateInventoryPrices(inventoryList);
        upateHotelMinPrice(hotel, inventoryList, startDate, endDate);
    }


    private void upateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate){

        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                       Inventory::getDate,
                       Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                        ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));

        // Prepare HotelPrice entities in bulk
        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date,price) -> {
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                    .orElse(new HotelMinPrice(hotel, date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        // Save all HotelPrice entities in bulk
        hotelMinPriceRepository.saveAll(hotelPrices);

    }

    private void updateInventoryPrices(List<Inventory> inventoryList){
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });

        inventoryRepository.saveAll(inventoryList);
    }
}
