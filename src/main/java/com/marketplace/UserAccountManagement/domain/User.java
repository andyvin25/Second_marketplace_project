package com.marketplace.UserAccountManagement.domain;

import java.util.HashSet;
import java.util.Set;

import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.marketplace.Util.Auditable;

@Entity(name = "User_UserAccountManagement")
@Table(name = "`Users`")
@AllArgsConstructor
@Builder
@Getter @Setter @NoArgsConstructor
public class User extends Auditable {
    
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    Set<Address> addresses = new HashSet<>();

    public User(String email, String name, String password) {
        this.setEmail(email);
        this.setName(name);
        this.password = this.doEncryptPassword(password);
//        this.setAccountRoles(sellerRoles);
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }

//    public void addRole(Role role) {
//        accountRoles.add(role);
//    }


    public String doEncryptPassword(String sellerPassword) {
        return BCrypt.hashpw(sellerPassword, BCrypt.gensalt(10));
    }

}
