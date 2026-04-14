package com.marketplace.Auth.api;

import com.marketplace.Auth.domain.*;
import com.marketplace.Util.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final UserMapper mapper;
    private final RoleService roleService;
    private final AuthService authService;
    private final PermissionService permissionService;

    @Operation(summary = "User can be register in here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully to create users",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDto.class)) }
            ),
            @ApiResponse(responseCode = "302", description = "the email has already been taken",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Email already registered\", \"cause\": \"Duplicate resource\"}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid inserted value",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "Invalid inserted value"
                    )) }
            )
    })
    @PostMapping("/register")
    public ResponseEntity<Object> createAccount(@RequestBody @Valid UserAccountCreationDTO accountDTO) {
        if (!accountDTO.repeatPassword().equals(accountDTO.password())) {
            throw new IllegalArgumentException("repeat password should match the password");
        }
        Permission BuyerRead = permissionService.getOrCreatePermission(Permission.PermissionsEnum.BUYER_READ);
        Permission BuyerCreate = permissionService.getOrCreatePermission(Permission.PermissionsEnum.BUYER_CREATE);
        Permission BuyerUpdate = permissionService.getOrCreatePermission(Permission.PermissionsEnum.BUYER_UPDATE);
        Permission BuyerDelete = permissionService.getOrCreatePermission(Permission.PermissionsEnum.BUYER_DELETE);

        Set<Permission> permissions = new HashSet<>();
        permissions.add(BuyerRead);
        permissions.add(BuyerCreate);
        permissions.add(BuyerUpdate);
        permissions.add(BuyerDelete);
        Role role = roleService.getOrCreateRoleAccount(Role.RoleEnum.BUYER, permissions);
        System.out.println("role in controller= " + role);
        userService.createUserAccount(role, mapper, accountDTO);
        return ResponseHandler.generateResponse("Register Successfully", HttpStatus.CREATED, "");
    }

    @Operation(summary = "User login and generate access token & refresh token")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Login successfully and token stored in cookies",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Login Successfully\", \"status\": 201, \"data\": \"\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "200", description = "User already logged in (token cookie still valid)",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "You are already logged in"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Invalid email format or empty password",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"email\": \"Invalid Email\", \"password\": \"password can't be empty\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "401", description = "Invalid email or password",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Bad credentials\", \"cause\": \"Unauthorized\"}"
                                    )
                            )
                    }
            )
    })
    @PostMapping("/login")
    public ResponseEntity<Object> signIn(@RequestBody @Valid UserLoginDto userLoginDto, @CookieValue(value = "token", required = false) String token, HttpServletResponse response) {
        if (token != null) {
            return ResponseEntity.ok("You are already logged in");
        }
        TokenDto authenticatedUser = authService.authenticateUser(userLoginDto);

        Cookie tokenCookie = new Cookie("token", authenticatedUser.userToken());

//        Adjust cookie later in production
        tokenCookie.setMaxAge(365 * 24 * 60 * 60);
        tokenCookie.setSecure(true);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", authenticatedUser.refreshToken());
        refreshTokenCookie.setMaxAge(365 * 24 * 60 * 60);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        return ResponseHandler.generateResponse("Login Successfully", HttpStatus.OK, "");
    }

    @Operation(summary = "User logout and clear authentication cookies")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully logout and cookies cleared",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = ""
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "200", description = "User is not logged in (no refresh token found)",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "You are not logged in"
                                    )
                            )
                    }
            )
    })
    @DeleteMapping("/logout")
    public ResponseEntity<Object> userLogout(@CookieValue(value = "refresh_token", required = false) String token, HttpServletResponse response) {
        if (token == null) {
            return ResponseEntity.ok("You are not logged in");
        }
        authService.userLogout(token);
        Cookie tokenCookie = new Cookie("token", "");

//        Adjust cookie later in production
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
//        return ResponseHandler.generateResponse("", HttpStatus.NO_CONTENT, "");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/refresh/refresh_token")
    public ResponseEntity<Object> refreshToken(@RequestParam("refresh_token") String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseHandler.generateResponse("User hasn't logged in", HttpStatus.BAD_REQUEST, "");
        }
        TokenDto authenticatedUser = authService.generateRefreshToken(refreshToken);

        Cookie tokenCookie = new Cookie("token", authenticatedUser.userToken());

//        Adjust cookie later in production
        tokenCookie.setMaxAge(365 * 24 * 60 * 60);
        tokenCookie.setSecure(true);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", authenticatedUser.refreshToken());
        refreshTokenCookie.setMaxAge(365 * 24 * 60 * 60);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/csrf_token")
//    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
//        return csrfToken;
//        String csrfToken = new ULID().nextULID();
//        System.out.println("csrfToken = " + csrfToken);
//        Cookie tokenCookie = new Cookie("CSRF_TOKEN", csrfToken);
////        Adjust cookie later in production
//        tokenCookie.setMaxAge(-1);
//        tokenCookie.setHttpOnly(false);
//        tokenCookie.setPath("/");
//        tokenCookie.setSecure(true);
//        response.addCookie(tokenCookie);
//        System.out.println("httpSession = " + httpSession);
//        return ResponseEntity.ok(csrfToken);
//    }

//    @GetMapping("/csrf_token")
//    public CsrfToken getCsrfToken(HttpServletRequest request) {
//        return (CsrfToken) request.getAttribute("_csrf");
//    }

}