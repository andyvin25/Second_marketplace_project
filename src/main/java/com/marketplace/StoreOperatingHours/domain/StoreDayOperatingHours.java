package com.marketplace.StoreOperatingHours.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "`Store_Day_Operating_Hours`")
@Getter @Setter @NoArgsConstructor
public class StoreDayOperatingHours {

    @EmbeddedId
    private StoreDayOperatingHoursKey id;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @MapsId("dayId")
    @JoinColumn(name = "day_id", nullable = false)
    private Day day;

    @Column(name = "operating_hours_start")
    private LocalTime operatingHoursStart;

    @Column(name = "operating_hours_end")
    private LocalTime operatingHoursEnd;

    public StoreDayOperatingHours(LocalTime operatingHoursStart, LocalTime operatingHoursEnd) {
        this.setOperatingHoursStart(operatingHoursStart);
        this.setOperatingHoursEnd(operatingHoursEnd);
    }
}
