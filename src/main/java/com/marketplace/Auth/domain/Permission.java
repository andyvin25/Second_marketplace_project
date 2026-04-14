package com.marketplace.Auth.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity(name = "Permission_Auth")
@Table(name = "`Permissions`")
@Getter @Setter @NoArgsConstructor
public class Permission {
    @Id
    @GeneratedId
    private String id;

    public enum PermissionsEnum {
        ADMIN_READ,
        ADMIN_CREATE,
        ADMIN_UPDATE,
        ADMIN_DELETE,

        SELLER_READ,
        SELLER_CREATE,
        SELLER_UPDATE,
        SELLER_DELETE,

        BUYER_READ,
        BUYER_UPDATE,
        BUYER_CREATE,
        BUYER_DELETE
    }
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PermissionsEnum name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    public Permission(PermissionsEnum name) {
        this.name = name;
    }
}
