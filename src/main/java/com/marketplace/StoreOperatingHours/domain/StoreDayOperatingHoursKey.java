package com.marketplace.StoreOperatingHours.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class StoreDayOperatingHoursKey implements Serializable {

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "day_id")
    private String dayId;
}
