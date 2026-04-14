package com.marketplace.ProductEtalationManagement.domain;

import com.marketplace.Util.GeneratedId;
import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Etalation")
@Table(name = "`Etalations`")
@Getter @Setter @NoArgsConstructor
public class Etalation extends Auditable {
    
    @Id
    @GeneratedId
    private String id;

    @Column(name = "etalation_name", length = 150, nullable = false)
    private String etalationName;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, referencedColumnName = "id")
    private Store store;

    public Etalation(String etalationName) {
        this.setEtalationName(etalationName);
    }

}
