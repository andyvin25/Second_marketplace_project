package com.marketplace.StoreOperatingHours.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Table(name = "`Days`")
@Data
public class Day {

    @Id
    @GeneratedId
    private String id;

    public enum DaysOfWeek {
        MINGGU, SENIN, SELASA, RABU, KAMIS, JUMAT, SABTU
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "day_name", length = 15, nullable = false, updatable = false)
    private DaysOfWeek dayName;

    @OneToMany(mappedBy = "day", fetch = FetchType.LAZY)
    Set<StoreDayOperatingHours> storeDayOperatingHours;

}
