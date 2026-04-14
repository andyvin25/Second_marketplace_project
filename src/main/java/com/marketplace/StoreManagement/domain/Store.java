package com.marketplace.StoreManagement.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Store_StoreManagement")
@Table(name = "`Stores`")
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Store extends Auditable {

    @Id
    @GeneratedId
    private String id;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, referencedColumnName = "id")
    private Account account;

    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private StoreDetail storeDetail;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "Store_Profiles",
        joinColumns = {
            @JoinColumn(name = "store_id", referencedColumnName = "id")
        }, inverseJoinColumns = {
            @JoinColumn(name = "profile_id", referencedColumnName = "id")
        }
    )
    private Profile storeProfile;

    public Store(String name) {
        this.setName(name);
    }

    public Store(String name, String description, Account account) {
        this.setName(name);
        this.setDescription(description);
        this.setAccount(account);
    }

}
