package com.marketplace.Auth.api;

import com.marketplace.Auth.domain.Role;
import com.marketplace.Auth.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component("UserAuthMapper")
public class UserMapper {

    public UserAccountDto toAccountDto(User user) {
        String email = user.getEmail();

        Set<Role.RoleEnum> roles = user.getAccountRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        return new UserAccountDto(email, roles);
//        return new UserAccountDto(email);
    }

    public User toUserAccount(UserAccountCreationDTO accountDTO) {
        return new User(accountDTO.email(), accountDTO.name(), accountDTO.password(), new HashSet<>());
    }

}
