package com.marketplace.CategoryManagement.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity(name = "Category_Category_Management")
@Table(name = "`Categories`")
@AllArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedId
    private String id;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "isDeleted")
    private Boolean isDeleted;

    public Category(String name) {
        setName(name);
    }

}
