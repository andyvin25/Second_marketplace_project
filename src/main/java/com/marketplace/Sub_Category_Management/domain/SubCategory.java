package com.marketplace.Sub_Category_Management.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "Sub_Category_SubCategoryManagement")
@Table(name = "`Sub_Categories`")
@Getter
@Setter
@NoArgsConstructor
public class SubCategory {

    @Id
    @GeneratedId
    private String id;

    @Column(name = "sub_category_name", length = 80, nullable = false)
    private String subCategoryName;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "isDeleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private Category category;

    public SubCategory(String name, Category category) {
        setSubCategoryName(name);
        setCategory(category);
    }
}
