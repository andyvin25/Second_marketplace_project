package com.marketplace.StoreOperatingHours.api;

import com.marketplace.StoreOperatingHours.domain.DayService;
import com.marketplace.StoreOperatingHours.domain.StoreDayOperatingHoursService;
import com.marketplace.StoreOperatingHours.domain.StoreService;
import com.marketplace.Util.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@AllArgsConstructor
public class StoreOperatingHoursController {

//    private final StoreService storeService;
    private final StoreDayOperatingHoursService storeDayOperatingHoursService;
    private final DayService dayService;

    @Operation(summary = "Initialize 7-day operating hours for a store (Sunday to Saturday)")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Successfully created store operating hours",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"successfully to create store operating hours\", \"status\": 201, \"data\": \"\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Store with this id is not found store123\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "409", description = "Operating hours already created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"The user has already make days now you should update it\"}"
                                    )
                            )
                    }
            )
    })
    @PostMapping("/{store_id}/day_operating_hours")
    public ResponseEntity<Object> createStoreOperatingHours(@PathVariable("store_id") String storeId) {
        storeDayOperatingHoursService.createStoreOperatingHours(storeId, dayService.getOrCreateDays());
        return ResponseHandler.generateResponse("successfully to create store operating hours", HttpStatus.CREATED, "");
    }

    @Operation(summary = "Update 7-day store operating hours (must provide exactly 7 entries)")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Store operating hours successfully updated",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "store operating hours has updated"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store not found or operating hours not initialized",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"You should create the operating hours first\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Invalid input (must contain 7 days or invalid hour range)",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "The day number is 7"
                                    )
                            )
                    }
            )
    })
    @PutMapping("/{store_id}/day_operating_hours")
    public ResponseEntity<Object> updateStoreOperatingHours(@PathVariable("store_id") String storeId, @RequestBody @Valid List<StoreRequestOperatingHoursDto> storeDtos) {
//        storeService.updateStoreOperatingHours(storeId, storeDtos);
        storeDayOperatingHoursService.updateStoreOperatingHours(storeId, storeDtos);
        return ResponseEntity.status(HttpStatus.OK).body("store operating hours has updated");
    }

    @Operation(summary = "Retrieve full 7-day store operating hours schedule")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieve store operating hours",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreOperatingHoursDto.class))
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store not found or operating hours not created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Store must have create the operating hours first\"}"
                                    )
                            )
                    }
            )
    })
   @GetMapping("/{store_id}/day_operating_hours")
   public ResponseEntity<List<StoreOperatingHoursDto>> getAllStoreSchedule(@PathVariable("store_id") String storeId) {
       return ResponseEntity
               .ok(storeDayOperatingHoursService.getAllStoreOperatingHoursSchedules(storeId));
   }

    @Operation(summary = "Retrieve today's operating hours for a store")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieve today's store operating hours",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreOperatingHoursDto.class))
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store not found or operating hours not initialized",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Store should create operating hours first\"}"
                                    )
                            )
                    }
            )
    })
   @GetMapping("/{store_id}/day_operating_hours/today")
   public ResponseEntity<StoreOperatingHoursDto> getStoreScheduleToday(@PathVariable("store_id") String storeId) {
       return ResponseEntity
               .ok(storeDayOperatingHoursService.getStoreOperatingHoursToday(storeId, dayService.getAllDays()));
   }

}
