package com.marketplace.StoreManagement.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Role_Repository_In_Store_Management")
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(Role.RoleEnum name);
}
