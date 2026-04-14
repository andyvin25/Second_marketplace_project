package com.marketplace.StoreManagement.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Permission_StoreManagement")
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
