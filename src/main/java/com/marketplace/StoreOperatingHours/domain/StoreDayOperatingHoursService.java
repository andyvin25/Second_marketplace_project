package com.marketplace.StoreOperatingHours.domain;

import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.StoreOperatingHours.api.StoreIdDto;
import com.marketplace.StoreOperatingHours.api.StoreOperatingHoursDto;
import com.marketplace.StoreOperatingHours.api.StoreRequestOperatingHoursDto;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service("Store_Operating_Hours_Service")
public class StoreDayOperatingHoursService {


    @Autowired
    private StoreDayOperatingHoursRepository storeDayOperatingHoursRepository;

    @Autowired
    private StoreRepository storeRepository;

    public Optional<Store> getStoreById(String storeId) {
        return storeRepository.findById(storeId);
    }

    public void saveStore(Store store) {
        Objects.requireNonNull(store);
        storeRepository.save(store);
    }

    public void createStoreOperatingHours(String storeId, Set<Day> days) {
        Store foundStore = getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store with this id is not found " + storeId));
        if (!foundStore.getStoreDayOperatingHours().isEmpty()) {
            throw new ResourceDuplicationException("The user has already make days now you should update it");
        }
//        input and sort day to become sunday in index 0 to saturday in index 6
        Set<StoreDayOperatingHours> storeDayOperatingHoursSet = days.stream()
                .map(day -> {
                    log.info("created Store Days is: {}", day);
                    StoreDayOperatingHours storeDayOperatingHours = new StoreDayOperatingHours();
                    StoreDayOperatingHoursKey storeDayOperatingHoursKey = new StoreDayOperatingHoursKey();
                    storeDayOperatingHoursKey.setStoreId(storeId);
                    storeDayOperatingHoursKey.setDayId(day.getId());
                    storeDayOperatingHours.setId(storeDayOperatingHoursKey);
                    storeDayOperatingHours.setDay(day);
                    storeDayOperatingHours.setStore(foundStore);
                    return storeDayOperatingHours;
                }).sorted(Comparator.comparing(storeDayOperatingHours -> storeDayOperatingHours
                        .getDay().getDayName()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        storeDayOperatingHoursRepository.saveAll(storeDayOperatingHoursSet);
    }

//    @CachePut(value = "store", key = "#storeId")
    public void updateStoreOperatingHours(String storeId, List<StoreRequestOperatingHoursDto> storeRequestOperatingHoursDtos) {
        List<StoreIdDto> storeIdDto = storeRepository.getStoreById(storeId);
        if (storeIdDto.isEmpty()) {
            throw new ResourceNotFoundException("Store with this id not found");
        }
        List<StoreDayOperatingHours> storeDayOperatingHoursList = storeDayOperatingHoursRepository.findAllByStoreId(storeId);
//        input and sort day to become sunday in index 0 to saturday in index 6
        storeDayOperatingHoursList.sort(Comparator.comparing(s -> s.getDay().getDayName().ordinal()));
        
        if (storeDayOperatingHoursList.size() != 7) {
            throw new ResourceNotFoundException("You should create the operating hours first");
        }
        if (storeRequestOperatingHoursDtos.isEmpty()) {
            throw new IllegalArgumentException("there is no update for the store day operating hours");
        }
        if (storeRequestOperatingHoursDtos.size() != 7) {
            throw new IllegalArgumentException("The day number is 7");
        }
        
        IntStream.range(0, storeDayOperatingHoursList.size()).forEach(i -> {
            StoreDayOperatingHours entity = storeDayOperatingHoursList.get(i);
            StoreRequestOperatingHoursDto dto = storeRequestOperatingHoursDtos.get(i);
            LocalTime operatingTimeStart = convertDtoToLocalTime(dto.operatingHourStart());
            LocalTime operatingTimeEnd = convertDtoToLocalTime(dto.operatingHoursEnd());
            if (operatingTimeStart == null || operatingTimeEnd == null) {
                entity.setOperatingHoursStart(null);
                entity.setOperatingHoursEnd(null);
            }
            entity.setOperatingHoursStart(operatingTimeStart);
            entity.setOperatingHoursEnd(operatingTimeEnd);
        });

        storeDayOperatingHoursRepository.saveAll(storeDayOperatingHoursList);

    }

    private LocalTime convertDtoToLocalTime(Integer operatingHours) {
        return operatingHours == null ? null : LocalTime.of(operatingHours, 0);
    }

   @Transactional(readOnly = true)
   public List<StoreOperatingHoursDto> getAllStoreOperatingHoursSchedules(String storeId) {
       List<StoreIdDto> storeIdDto = storeRepository.getStoreById(storeId);
       if (storeIdDto.isEmpty()) {
           throw new ResourceNotFoundException("Store with this id not found");
       }
        List<StoreDayOperatingHours> storeDayOperatingHoursList = storeDayOperatingHoursRepository.findAllByStoreId(storeId);
        storeDayOperatingHoursList.sort(Comparator.comparing(s -> s.getDay().getDayName().ordinal()));

        List<StoreOperatingHoursDto> storeOperatingHoursDtos = storeDayOperatingHoursList.stream()
                        .map(storeDayOperatingHours -> new StoreOperatingHoursDto(storeDayOperatingHours.getDay().getDayName().name(), storeDayOperatingHours.getOperatingHoursStart(), storeDayOperatingHours.getOperatingHoursEnd()))
                .toList();

       if (storeOperatingHoursDtos.size() != 7) {
           throw new ResourceNotFoundException("Store must have create the operating hours first");
       }
       return storeOperatingHoursDtos;
   }


    @Transactional(readOnly = true)
    public StoreOperatingHoursDto getStoreOperatingHoursToday(String storeId, List<Day> days) {
        List<StoreIdDto> storeIdDto = storeRepository.getStoreById(storeId);
        if (storeIdDto.isEmpty()) {
            throw new ResourceNotFoundException("Store with this id not found");
        }

        List<StoreDayOperatingHours> storeDayOperatingHoursList = storeDayOperatingHoursRepository.findAllByStoreId(storeId);
        storeDayOperatingHoursList.sort(Comparator.comparing(s -> s.getDay().getDayName().ordinal()));

        if (storeDayOperatingHoursList.isEmpty()) {
            throw new ResourceNotFoundException("Store should create operating hours first");
        }

        Calendar calendar = Calendar.getInstance();

        Day dayName = days.get(calendar.get(Calendar.DAY_OF_WEEK) - 1);


        // search the current day from days

        StoreOperatingHoursDto storeOperatingHoursDto = IntStream.range(0, days.size())
            .filter(i -> {
                Day day = days.get(i);
                StoreDayOperatingHours storeDayOperatingHours = storeDayOperatingHoursList.get(i);
                return day.getDayName().equals(storeDayOperatingHours.getDay().getDayName());
            }).mapToObj(i -> {
                StoreDayOperatingHours operatingHours = storeDayOperatingHoursList.get(i);
                return new StoreOperatingHoursDto(dayName.getDayName().name(), operatingHours.getOperatingHoursStart(), operatingHours.getOperatingHoursEnd());
            }).findFirst().orElse(null);


        return storeOperatingHoursDto;
    }

}
