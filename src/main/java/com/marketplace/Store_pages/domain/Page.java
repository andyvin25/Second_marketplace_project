package com.marketplace.Store_pages.domain;

import com.marketplace.Util.GeneratedId;
import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Page")
@Table(name = "`Pages`")
@Getter @Setter @NoArgsConstructor
public class Page extends Auditable {
    
    @Id
    @GeneratedId
    private String id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, referencedColumnName = "id")
    private Store store;

    public Page(String name) {
        this.setName(name);
    }

}
