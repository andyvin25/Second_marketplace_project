package com.marketplace.StoreManagement.api;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.marketplace.StoreManagement.domain.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.ResponseHandler;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@MultipartConfig(maxFileSize = 3 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
@RestController
@RequestMapping("/api")
public class StoreManagementController {
    
    private final StoreService storeService;
    private final AccountService accountService;
    private final PermissionService permissionService;
    private final StoreMapper mapper;
    private final RoleService roleService;

    @Value("${file.upload.profile}")
    private String uploadDir;

    public StoreManagementController(StoreService storeService,
                                     AccountService accountService,
                                     PermissionService permissionService,
                                     StoreMapper storeMapper,
                                     RoleService roleService) {
        this.storeService = storeService;
        this.accountService = accountService;
        this.permissionService = permissionService;
        this.mapper = storeMapper;
        this.roleService = roleService;
    }

    @Operation(summary = "Retrieve all stores (id and name only)")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieve all stores",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreProjection.class))
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('ADMIN_READ')")
    @GetMapping("/stores")
    public ResponseEntity<Object> getAllStores() {
        List<StoreProjection> outputs = storeService.getAllIdAndNameStores();
        log.info("Admin get stores object: {}", outputs);
        return ResponseHandler.generateResponse("Successfully to retrieve the stores", HttpStatus.OK, outputs);
    }

    @Operation(summary = "Get store information by seller account id")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieve seller store",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreDto.class))
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"resource not found\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Seller has not created a store yet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "You need to create store first"
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('BUYER_READ', 'SELLER_READ', 'ADMIN_READ')")
    @GetMapping("/sellers/{account_id}/stores/store")
    public ResponseEntity<Object> getStoreWithAnAccount(@PathVariable("account_id") String accountId) {
        log.info("Get initialize /api/sellers/{account_id}/stores/store");
        Store store = accountService.getStoreWithAccount(accountId);
        StoreDto output = mapper.toStoreDto(store);
        return ResponseHandler.generateResponse("Get the active store from the seller", HttpStatus.OK, output);
    }

//    @Operation(summary = "Check whether seller account already has a store")
//    @ApiResponses(value = {
//
//            @ApiResponse(responseCode = "200", description = "Return true if seller has store, false otherwise",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(
//                                            example = "true"
//                                    )
//                            )
//                    }
//            ),
//
//            @ApiResponse(responseCode = "404", description = "Account not found",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(
//                                            example = "{\"message\": \"User account with this id not found\"}"
//                                    )
//                            )
//                    }
//            )
//    })
//    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'SELLER_READ', 'BUYER_READ)")
//    @GetMapping("/sellers/{account_id}/check")
//    public ResponseEntity<Boolean> doesAccountHaveStore(@PathVariable("account_id") String accountId) {
//        Boolean checkAccountHasStore = accountService.doesAccountHaveStore(accountId);
//        return ResponseEntity.ok(checkAccountHasStore);
//    }

    @Operation(summary = "Retrieve store logo file path")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieve store logo path",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "/uploads/store123/profile/profile_store123.png"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store or file not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"The store with this id not found\"}"
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('BUYER_READ', 'SELLER_READ', 'ADMIN_READ')")
    @GetMapping("/stores/{store_id}/profile")
    public ResponseEntity<Object> getStoreLogo(@PathVariable("store_id") String storeId) {
        
        String storeLogo = storeService.getStoreLogo((storeId));
        System.out.println("storeLogo = " + storeLogo);
        return ResponseHandler.generateResponse("Successfully retrive logo", HttpStatus.OK, storeLogo);
    }

    @Operation(summary = "Upload or update store logo image (PNG, JPG, JPEG only, max 2MB)")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Image successfully uploaded",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "/uploads/store123/profile/profile_store123.png"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Invalid file type or file size exceeds 2MB",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "Invalid file type. Only PNG or JPEG or JPG files are allowed"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"The store with this id not found\"}"
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAuthority('SELLER_CREATE')")
    @PostMapping("/stores/{store_id}/upload_image/image")
    public ResponseEntity<Object> updateStoreLogo(@PathVariable("store_id") String storeId, @RequestPart("image") MultipartFile file) {
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id not found"));
        if (!file.getContentType().equals("image/png") &&
                !file.getContentType().equals("image/jpeg") &&
                !file.getContentType().equals("image/jpg")
        ) {
            throw new IllegalArgumentException("Invalid file type. Only PNG or JPEG or JPG files are allowed");
        }
        try {
            String uploadedFile = storeService.uploadStoreProfile(store, file, uploadDir);
            return ResponseHandler.generateResponse("Image successfully to uploaded", HttpStatus.CREATED, uploadedFile);

        } catch (IOException e) {
            log.error("file upload error" + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());

        }

    }

    @Operation(summary = "Delete store logo image")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Image successfully deleted",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = ""
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store or image file not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"The file is not found\"}"
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('ADMIN_DELETE', 'SELLER_DELETE')")
    @DeleteMapping("/stores/{store_id}/uploaded_image")
    public ResponseEntity<Object> deleteStoreLogo(@PathVariable("store_id") String storeId) {
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
        storeService.deleteStoreLogo(store);
        return ResponseHandler.generateResponse("Image successfully to deleted", HttpStatus.OK, "");

    }
    
    // let's keep it to this way first with {accountId} stuff
//    and then check the testting for this
    @Operation(summary = "Create store for seller account")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Store successfully created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Store created\", \"status\": 201, \"data\": \"\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Invalid store name (validation error)",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"store_name\": \"Invalid store name: Empty/Null\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "409", description = "Store name already taken",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"The name of the store is already been taken\"}"
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAuthority('BUYER_CREATE')")
    @PostMapping("/sellers/{account_id}/stores")
    public ResponseEntity<Object> createStore(@PathVariable("account_id") String accountId, @RequestBody @Valid StoreRequestDTO storeDto) {
        System.out.println("store dto is: " + storeDto);
        Account account = accountService.getAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no account with this id " + accountId));
        Set<Permission> permissions = new HashSet<>();
        Permission permissionRead = permissionService.getOrCreatePermission(Permission.PermissionsEnum.SELLER_READ);
        Permission permissionCreate = permissionService.getOrCreatePermission(Permission.PermissionsEnum.SELLER_CREATE);
        Permission permissionUpdate = permissionService.getOrCreatePermission(Permission.PermissionsEnum.SELLER_UPDATE);
        Permission permissionDelete = permissionService.getOrCreatePermission(Permission.PermissionsEnum.SELLER_DELETE);
        permissions.add(permissionCreate);
        permissions.add(permissionRead);
        permissions.add(permissionUpdate);
        permissions.add(permissionDelete);

        Boolean checkIntersectionStoreName = storeService.hasStoreNameSame(storeDto.storeName());
        System.out.println("checkIntersectionStoreName = " + checkIntersectionStoreName);
        Role role = roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER, permissions);
        System.out.println("role passed = " + role);
        accountService.createStore(account, role, checkIntersectionStoreName, storeDto);
//        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore);
        return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, "");
    }

    @Operation(summary = "Update seller store name")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Store name successfully changed",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "NewStoreName"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Seller account not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Seller with this id is not found\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Store has not been created yet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "Store has not created yet"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "409", description = "Store name already taken",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"The name of the store is already been taken\"}"
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAuthority('SELLER_UPDATE')")
    @PutMapping("/sellers/{account_id}/stores/store")
    public ResponseEntity<Object> updateStoreName(@PathVariable("account_id") String accountId, @RequestBody @Valid StoreRequestDTO storeDto) {
        String storeUpdatedName = accountService.updateStoreName(accountId, storeService.hasStoreNameSame(storeDto.storeName()), storeDto);
        return ResponseHandler.generateResponse("Store name successfully changes", HttpStatus.CREATED, storeUpdatedName);
    }

    @Operation(summary = "Delete seller store")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Store successfully deleted",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = ""
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Seller account not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Seller with this id not found\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Store has not been created yet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "Store has not created yet"
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('ADMIN_DELETE', 'SELLER_DELETE')")
    @DeleteMapping("/sellers/{account_id}/stores/store")
    public ResponseEntity<Object> deleteStore(@PathVariable("account_id") String accountId) {
        accountService.deleteStoreFromAccount(accountId);
        return ResponseHandler.generateResponse("Store name successfully changes", HttpStatus.CREATED, "");
    }

    @Operation(summary = "Delete seller account and associated store")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Account and store successfully deleted",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "account and store has been deleted"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Seller account not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Seller with this id not found\"}"
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('ADMIN_DELETE', 'BUYER_DELETE')")
    @DeleteMapping("/sellers/delete/{account_id}")
    public ResponseEntity<String> deleteAccountAndStore(@PathVariable("account_id") String accountId) {
        accountService.deleteAccountAndStore(accountId);
        return ResponseEntity.ok("account and store has been deleted");
    }

}
