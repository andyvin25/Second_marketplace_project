package com.marketplace.StoreOperatingHours.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;


@RegisterReflectionForBinding
public record StoreRequestOperatingHoursDto(
   @JsonProperty
   @Nullable
   @Min(value = 0, message = "Operating hour start must be ≥0")
   @Max(value = 23, message = "Operating hour start must be ≤23")
   Integer operatingHourStart,

   @JsonProperty
   @Nullable
   @Min(value = 0, message = "Operating hour end must be ≥0")
   @Max(value = 23, message = "Operating hour end must be ≤23")
   Integer operatingHoursEnd
) {
//
//    public StoreRequestOperatingHoursDto {
//        if (toLocalTime(operatingHourStart, operatingMinutesStart)
//                .isAfter(toLocalTime(operatingHoursEnd, operatingMinutesEnd))) {
//            throw new IllegalArgumentException("Start time must be before end time");
//        }
//    }
//
//    public LocalTime operatingTimeStart() {
//        return toLocalTime(operatingHourStart, operatingMinutesStart);
//    }
//
//    public LocalTime operatingTimeEnd() {
//        return toLocalTime(operatingHoursEnd, operatingMinutesEnd);
//    }
//
//    private static LocalTime toLocalTime(int hour, int minute) {
//        return LocalTime.of(hour, minute);
//    }
//
}

// @Data @NoArgsConstructor @AllArgsConstructor
// public class StoreRequestOperatingHoursDto {
//     @Min(value = 0, message = "Operating hour start must be ≥0")
//     @Max(value = 23, message = "Operating hour start must be ≤23")
//     private Integer operatingHourStart;
//     @Min(value = 0, message = "Operating minute start must be ≥0")
//     @Max(value = 59, message = "Operating minute start must be ≤59")
//     private Integer operatingMinutesStart;
//     @Min(value = 0, message = "Operating hour end must be ≥0")
//     @Max(value = 23, message = "Operating hour end must be ≤23")
//     private Integer operatingHoursEnd;
//     @Min(value = 0, message = "Operating minute end must be ≥0")
//     @Max(value = 59, message = "Operating minute end must be ≤59")
//     private Integer operatingMinutesEnd;

// }
