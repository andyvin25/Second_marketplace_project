package com.marketplace.StoreManagement.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Role_inStore")
@Table(name = "`Roles`")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedId
    private String id;

    public enum RoleEnum {
        DEVELOPER,
        SELLER,
        ADMIN,
        BUYER
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 50, nullable = false)
    private RoleEnum roleName;

    @ManyToMany(mappedBy = "accountRoles")
    private Set<Account> sellers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Roles_Permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    public Role(RoleEnum roleName) {
        this.setRoleName(roleName);
    }

    public Role(RoleEnum roleName, Set<Permission> permissions) {
        this.setRoleName(roleName);
        this.setPermissions(permissions);
    }

    public void addSeller(Account account) {
        sellers.add(account);
        setPermissions(permissions);
    }
}
