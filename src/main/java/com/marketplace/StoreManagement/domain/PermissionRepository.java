package com.marketplace.StoreManagement.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("PermissionRepository_StoreManagement")
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findByName(Permission.PermissionsEnum name);

}
