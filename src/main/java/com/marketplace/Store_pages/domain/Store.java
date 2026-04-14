package com.marketplace.Store_pages.domain;

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

@Entity(name = "StorePages")
@Table(name = "`Stores`")
@Getter @NoArgsConstructor
public class Store extends Auditable {

    @Id
    @GeneratedId
    private String id;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Page> pages = new HashSet<Page>();

    public void addPages(Page page) {
        pages.add(page);
    }

    // public void deletePageWithId(String pageId) {
    //     pages.removeIf(foundPage -> foundPage.getId().equals(pageId));
    // }

}
