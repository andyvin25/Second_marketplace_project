package com.marketplace.Auth.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("Permission_Service_Auth")
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public Permission getOrCreatePermission(Permission.PermissionsEnum permissionEnum) {
        Permission permission = permissionRepository.findByName(permissionEnum).orElse(null);
        if (permission == null) {
            permission = new Permission(permissionEnum);
            permissionRepository.save(permission);
        }

        return  permission;
    }
}
