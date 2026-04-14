package com.marketplace.StoreManagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`Store_Details`")
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StoreDetail {

    @Id
    @Column(name = "store_id")
    private String id;

    @Column(name = "rate", nullable = false)
    private int rate;

    public enum StoreStatusEnum {
        BRONZE,
        GOLD,
        PLATINUM
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StoreStatusEnum status;

    @Column(name = "number_of_sales", nullable = false)
    private int numberOfSales;

    @OneToOne
    @MapsId
    @JoinColumn(name = "store_id")
    private Store store;

}

//     public StoreView(int rate, StoreStatusEnum status, int numberOfSales, Store store) {
//         this.setRate(rate);
//         this.setStatus(status);
//         this.setNumberOfSales(numberOfSales);
//         this.setStore(store);
//     }
