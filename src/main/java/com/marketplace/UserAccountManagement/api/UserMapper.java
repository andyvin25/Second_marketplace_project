package com.marketplace.UserAccountManagement.api;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.marketplace.UserAccountManagement.domain.User;

@Component
public class UserMapper {
    
    public UserAccountDTO toAccountDto(User user) {
        String id = user.getId();
        String name = user.getName();
        String email = user.getEmail();

        return new UserAccountDTO(id, email, name);
    }
}
