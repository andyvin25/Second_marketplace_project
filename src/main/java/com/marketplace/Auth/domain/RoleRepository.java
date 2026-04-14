package com.marketplace.Auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Role_Repository_Auth")
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByRoleName(Role.RoleEnum roleName);
}
