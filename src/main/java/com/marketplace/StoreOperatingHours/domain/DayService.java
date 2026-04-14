package com.marketplace.StoreOperatingHours.domain;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
public class DayService {

    @Autowired
    private DayRepository dayRepository;

    public void saveDays(Set<Day> days) {
        Objects.requireNonNull(days);
        dayRepository.saveAll(days);
    }

    public List<Day> getAllDays() {
        return dayRepository.findAll().stream().collect(Collectors.toCollection(LinkedList::new));
    }

    public Set<Day> getOrCreateDays() {
        List<Day> days = getAllDays();
        if (days.size() < 7) {
            Day.DaysOfWeek[] daysEnumVal = Day.DaysOfWeek.values();
            Set<Day> createDays = IntStream.range(0, DayOfWeek.values().length).mapToObj(i -> {
                Day day = new Day();
                day.setDayName(daysEnumVal[i]);
                return day;
            }).collect(Collectors.toCollection(LinkedHashSet::new));
            saveDays(createDays);
            return createDays;
        }
        log.info("Get Days from day database: {}", days);
        return new HashSet<>(days).stream().collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
