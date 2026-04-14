package com.marketplace.Auth.domain;

import com.marketplace.Util.Auditable;
import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.*;

@Entity(name = "User_Auth")
@Table(name = "`Users`")
@Getter
@Setter
@NoArgsConstructor
@Log4j2
@AllArgsConstructor
@Builder
public class User extends Auditable implements UserDetails {

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "`Account_Roles`",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> accountRoles;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    @CreatedDate
//    private Date createdAt;
//
//    @Column(name = "updated_times", nullable = false)
//    @LastModifiedDate
//    private Date updatedTimes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    public User(String email, String name, String password, Set<Role> sellerRoles) {
        this.setEmail(email);
        this.setName(name);
        this.password = this.doEncryptPassword(password);
        this.setAccountRoles(sellerRoles);
    }

    public void addRole(Role role) {
        accountRoles.add(role);
    }

    public String doEncryptPassword(String sellerPassword) {
        return BCrypt.hashpw(sellerPassword, BCrypt.gensalt(10));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : this.getAccountRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName().name()));
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName().name()));
            }
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }
}