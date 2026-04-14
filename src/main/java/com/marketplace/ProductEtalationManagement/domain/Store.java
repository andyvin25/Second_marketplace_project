package com.marketplace.ProductEtalationManagement.domain;

import java.util.HashSet;
import java.util.Set;

import com.marketplace.Util.GeneratedId;
import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "EtalationStore")
@Table(name = "`Stores`")
@Getter @Setter @NoArgsConstructor
public class Store extends Auditable{
    
    @Id
    @GeneratedId
    private String id;

    @Column(name = "name", length = 30, nullable = false)
    private String storeName;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Etalation> storeEtalations = new HashSet<>();

    public void addStoreEtalation(Etalation etalation) {
        storeEtalations.add(etalation);
    }

}
