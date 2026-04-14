package com.marketplace.StoreManagement;

import com.marketplace.StoreManagement.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataSeederTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private RoleService roleService;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private DataSeeder dataSeeder;

    @Mock
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(dataSeeder, "admin_name", "admin");
        ReflectionTestUtils.setField(dataSeeder, "admin_password", "password");
        ReflectionTestUtils.setField(dataSeeder, "admin_email", "admin@email.com");
    }

    @Test
    public void initiateAdminUser_WhenAdminDoesNotExist_SeedAdminUser() throws Exception {
        // Arrange
        when(accountRepository.findByEmail("admin@email.com")).thenReturn(Optional.empty());

        when(permissionService.getOrCreatePermission(any(Permission.PermissionsEnum.class)))
                .thenAnswer(invocation -> new Permission(invocation.getArgument(0)));
        
        when(roleService.getOrCreateRoleAccount(any(Role.RoleEnum.class), any()))
                .thenAnswer(invocation -> new Role(invocation.getArgument(0), invocation.getArgument(1)));

        // Act
        CommandLineRunner runner = dataSeeder.initiateAdminUser(accountRepository, storeService, roleService, permissionService);
        runner.run();

        // Assert
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(accountCaptor.capture());

        Account savedAccount = accountCaptor.getValue();
        assertThat(savedAccount.getEmail()).isEqualTo("admin@email.com");
        assertThat(savedAccount.getName()).isEqualTo("admin");
        assertThat(savedAccount.getAccountRoles()).hasSize(2);
        assertThat(savedAccount.getStores()).hasSize(1);
    }

    @Test
    public void initiateAdminUser_WhenAdminExists_DoNothing() throws Exception {
        // Arrange
        when(accountRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(new Account()));

        // Act
        CommandLineRunner runner = dataSeeder.initiateAdminUser(accountRepository, storeService, roleService, permissionService);
        runner.run();

        // Assert
        verify(accountRepository, never()).save(any());
        verify(permissionService, never()).getOrCreatePermission(any());
    }
}
