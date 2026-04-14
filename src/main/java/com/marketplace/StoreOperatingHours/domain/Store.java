package com.marketplace.StoreOperatingHours.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Store_Operation_Hours")
@Table(name = "`Stores`")
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Store {

    @Id
    @GeneratedId
    private String id;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private Set<StoreDayOperatingHours> storeDayOperatingHours = new HashSet<>();

//    public void addStoreDayOperatingHours(StoreDayOperatingHours storeDayOperatingHours) {
//        storeDayOperatingHours
//    }
}
