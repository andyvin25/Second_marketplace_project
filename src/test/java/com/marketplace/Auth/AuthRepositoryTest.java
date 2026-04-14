package com.marketplace.Auth;

import com.marketplace.Auth.domain.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;


    @Test
    @Rollback(value = false)
    public void saveUser_thenReturnSuccess() {

        String email = "email@test.ts";
        String username = "username";
        String password = "password";

        Set<Permission> permissions = new HashSet<>();

        permissions.add(
                permissionRepository.save(new Permission(Permission.PermissionsEnum.BUYER_READ))
        );
        permissions.add(
                permissionRepository.save(new Permission(Permission.PermissionsEnum.BUYER_UPDATE))
        );
        permissions.add(
                permissionRepository.save(new Permission(Permission.PermissionsEnum.BUYER_CREATE))
        );
        permissions.add(
                permissionRepository.save(new Permission(Permission.PermissionsEnum.BUYER_DELETE))
        );

        Set<Role> roles = new HashSet<>();

        Role role1 = getOrCreateRoleAccount(Role.RoleEnum.BUYER, permissions);

        roles.add(role1);

        User user = User.builder()
                .email(email)
                .name(username)
                .password(password)
                .accountRoles(roles)
                .isDeleted(false)
//                .createdAt(new Date())
//                .updatedTimes(new Date())
                .build();
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getName()).isEqualTo(username);
        assertThat(savedUser.getAccountRoles()).isEqualTo(roles);

        List<String> permissionList = savedUser.getAccountRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream()) // stream of Permission
                .map(permission -> permission.getName().name())  // PermissionsEnum -> String
                .toList();

//        assertThat(permissionList).isEqualTo(permissions.stream().toList());

        assertThat(permissionList).isEqualTo(
                permissions.stream()
                        .map(permission -> permission.getName().name())
                        .toList()
        );

    }

    @Test
    @Rollback(value = false)
    public void findActiveUser_thenReturnSuccess() {
        String email = "email@test.ts";
        String username = "username";
        String password = "password";

        Set<Permission> permissions = new HashSet<>();

        permissions.add(
                permissionRepository.save(new Permission(Permission.PermissionsEnum.BUYER_READ))
        );
        permissions.add(
                permissionRepository.save(new Permission(Permission.PermissionsEnum.BUYER_UPDATE))
        );
        permissions.add(
                permissionRepository.save(new Permission(Permission.PermissionsEnum.BUYER_CREATE))
        );
        permissions.add(
                permissionRepository.save(new Permission(Permission.PermissionsEnum.BUYER_DELETE))
        );

        Set<Role> roles = new HashSet<>();

        Role role1 = getOrCreateRoleAccount(Role.RoleEnum.BUYER, permissions);

        roles.add(role1);

        User user = User.builder()
                .email(email)
                .name(username)
                .password(password)
                .accountRoles(roles)
                .isDeleted(false)
//                .createdAt(new Date())
//                .updatedTimes(new Date())
                .build();
        userRepository.save(user);

        Optional<User> user1 = userRepository.findActiveByEmail(email);

        System.out.println("user1.get().getAccountRoles() = " + user1.get().getAccountRoles());
        
        assertThat(user1.get().getId()).isNotNull();
        assertThat(user1.get().getEmail()).isEqualTo(email);
        assertThat(user1.get().getName()).isEqualTo(username);
        assertThat(user1.get().getAccountRoles()).isEqualTo(roles);


    }


    @Transactional
    private Role getOrCreateRoleAccount(Role.RoleEnum roleName, Set<Permission> permissionSet) {
        Role role = roleRepository.findByRoleName(roleName).orElse(null);
        if (role == null) {
            Role newRole = new Role(roleName, permissionSet);
            roleRepository.save(newRole);
            return newRole;
        }

        return role;
    }
}
