package com.marketplace.StoreManagement.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service("role_service_inStore_management")
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public void saveRole(Role role) {
        Objects.requireNonNull(role);
        roleRepository.save(role);
    }

    public Role getOrCreateRoleAccount(Role.RoleEnum roleEnum, Set<Permission> permissions) {
        Role role = roleRepository.findByRoleName(roleEnum).orElse(null);
        if (role != null && !role.getPermissions().isEmpty()) {
            permissions
                    .forEach(permission -> {
                        role.getPermissions().add(permission);
                    });
        }
        if (role == null) {
            Role newRole = new Role(roleEnum, permissions);
            saveRole(newRole);
            return newRole;
        }

        return role;
    }
}