package com.marketplace.Auth;

import com.marketplace.Auth.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailService userDetailService;

    private User user;
    private Set<Permission> permissions;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        // setup permissions
        permissions = new HashSet<>();
        permissions.add(new Permission(Permission.PermissionsEnum.BUYER_READ));
        permissions.add(new Permission(Permission.PermissionsEnum.BUYER_CREATE));
        permissions.add(new Permission(Permission.PermissionsEnum.BUYER_UPDATE));
        permissions.add(new Permission(Permission.PermissionsEnum.BUYER_DELETE));

        // setup roles
        roles = new HashSet<>();
        roles.add(new Role(Role.RoleEnum.BUYER, permissions));

        // setup user
        user = User.builder()
                .id("user-id-1")
                .email("user@test.com")
                .name("Test User")
                .password("password")
                .isDeleted(false)
                .accountRoles(roles)
                .build();
    }

    // ===================== loadUserByUsername =====================
    @Test
    void whenValidEmail_thenReturnUserDetails() {
        when(userService.getUserByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailService.loadUserByUsername("user@test.com");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("user@test.com");
    }

    @Test
    void whenUserNotFound_thenThrowUsernameNotFoundException() {
        when(userService.getUserByEmail("notfound@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailService.loadUserByUsername("notfound@test.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("notfound@test.com");
    }

    @Test
    void whenValidEmail_thenUserDetailsHasCorrectRoles() {
        when(userService.getUserByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailService.loadUserByUsername("user@test.com");
        System.out.println("result.getAuthorities() = " + result.getAuthorities());
        User resultUser = (User) result;
        System.out.println("resultUser.getAccountRoles() = " + resultUser.getAccountRoles());
        System.out.println("resultUser.getAuthorities() = " + resultUser.getAuthorities());
        
        assertThat(resultUser.getAccountRoles()).isNotNull();
        assertThat(resultUser.getAccountRoles()).isEqualTo(roles);
    }

    @Test
    void whenValidEmail_thenUserDetailsHasCorrectPermissions() {
        when(userService.getUserByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailService.loadUserByUsername("user@test.com");
        User resultUser = (User) result;

        // get permissions from roles
        List<String> resultPermissions = resultUser.getAccountRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getName().name())
                .toList();

        List<String> expectedPermissions = permissions.stream()
                .map(permission -> permission.getName().name())
                .toList();

        assertThat(resultPermissions).containsExactlyInAnyOrderElementsOf(expectedPermissions);
    }

    @Test
    void whenValidEmail_thenGetAuthoritiesNotNull() {
        when(userService.getUserByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailService.loadUserByUsername("user@test.com");

        assertThat(result.getAuthorities()).isNotNull();
        assertThat(result.getAuthorities()).isNotEmpty();
    }

    @Test
    void whenValidEmail_thenAuthoritiesContainPermissions() {
        when(userService.getUserByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailService.loadUserByUsername("user@test.com");

        List<String> authorityNames = result.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // check role is included
        assertThat(authorityNames).contains("BUYER");

        // check permissions are included
        assertThat(authorityNames).contains(
                "BUYER_READ",
                "BUYER_CREATE",
                "BUYER_UPDATE",
                "BUYER_DELETE"
        );
    }
}
