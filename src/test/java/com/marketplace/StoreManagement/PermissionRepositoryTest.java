package com.marketplace.StoreManagement;

import com.marketplace.StoreManagement.domain.Permission;
import com.marketplace.StoreManagement.domain.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    public void savePermission_thenReturnSuccess() {
        Permission permission = new Permission(Permission.PermissionsEnum.ADMIN_READ);
        Permission savedPermission = permissionRepository.save(permission);

        assertThat(savedPermission.getId()).isNotNull();
        assertThat(savedPermission.getName()).isEqualTo(Permission.PermissionsEnum.ADMIN_READ);
    }

    @Test
    public void findByName_thenReturnSuccess() {
        Permission permission = new Permission(Permission.PermissionsEnum.SELLER_CREATE);
        permissionRepository.save(permission);

        Optional<Permission> foundPermission = permissionRepository.findByName(Permission.PermissionsEnum.SELLER_CREATE);

        assertThat(foundPermission).isPresent();
        assertThat(foundPermission.get().getName()).isEqualTo(Permission.PermissionsEnum.SELLER_CREATE);
    }
}
