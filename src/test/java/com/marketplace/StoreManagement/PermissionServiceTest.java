package com.marketplace.StoreManagement;

import com.marketplace.StoreManagement.domain.Permission;
import com.marketplace.StoreManagement.domain.PermissionRepository;
import com.marketplace.StoreManagement.domain.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    public void getOrCreatePermission_WhenExists_ReturnExisting() {
        // Arrange
        Permission.PermissionsEnum permissionEnum = Permission.PermissionsEnum.ADMIN_READ;
        Permission existingPermission = new Permission(permissionEnum);
        when(permissionRepository.findByName(permissionEnum)).thenReturn(Optional.of(existingPermission));

        // Act
        Permission result = permissionService.getOrCreatePermission(permissionEnum);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(permissionEnum);
        verify(permissionRepository, never()).save(any(Permission.class));
    }

    @Test
    public void getOrCreatePermission_WhenNotExists_CreateAndReturnNew() {
        // Arrange
        Permission.PermissionsEnum permissionEnum = Permission.PermissionsEnum.SELLER_CREATE;
        when(permissionRepository.findByName(permissionEnum)).thenReturn(Optional.empty());
        when(permissionRepository.save(any(Permission.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Permission result = permissionService.getOrCreatePermission(permissionEnum);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(permissionEnum);
        verify(permissionRepository, times(1)).save(any(Permission.class));
    }
}
