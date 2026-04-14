package com.marketplace.Sub_Category_Management.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Category_Sub_Category_Management")
@Table(name = "`Categories`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedId
    private String id;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "isDeleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<SubCategory> subCategories = new HashSet<>();

    public Category(Set<SubCategory> subCategories) {
        setSubCategories(subCategories);
    }
}
