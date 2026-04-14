package com.marketplace.StoreManagement.domain;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.*;

import com.marketplace.Util.Auditable;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "User_StoreManagement")
@Table(name = "`Users`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Account extends Auditable {
    
    @Id
    @GeneratedId
    private String id;

    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "`Account_Roles`",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> accountRoles = new HashSet<Role>();

    @OneToMany(mappedBy = "account", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private Set<Store> stores = new HashSet<>();

    public Account(String email, String name, String password, Set<Role> sellerRoles, Set<Store> stores) {
        this.setEmail(email);
        this.setName(name);
        this.password = this.doEncryptPassword(password);
        this.setAccountRoles(sellerRoles);
        this.setStores(stores);
    }

    public void addRole(Role role) {
        accountRoles.add(role);
    }

    public String doEncryptPassword(String sellerPassword) {
        return BCrypt.hashpw(sellerPassword, BCrypt.gensalt(10));
    }

}
