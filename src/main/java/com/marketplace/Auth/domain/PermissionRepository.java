package com.marketplace.Auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Permission_Auth_Repository")
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findByName(Permission.PermissionsEnum name);
}
