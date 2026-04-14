package com.marketplace.StoreOperatingHours.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.time.LocalTime;

@RegisterReflectionForBinding
public record StoreOperatingHoursDto(
        String dayName,
        LocalTime storeOperatingTimeStart,
        LocalTime storeOperatingTimeEnd
) {
}
