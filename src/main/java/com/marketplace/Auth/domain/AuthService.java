package com.marketplace.Auth.domain;

import com.marketplace.Auth.api.TokenDto;
import com.marketplace.Auth.api.UserLoginDto;
import com.marketplace.Exception.RefreshTokenExpireException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Value("${jwt.refresh_expiration}")
    private int jwtRefreshTokenExpirationMs;

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    RefreshTokenService refreshTokenService;

    public TokenDto authenticateUser(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.email(),
                        userLoginDto.password()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User getUserByEmail = userService.getActiveUserByEmail(userDetails.getUsername());
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        String refreshToken = refreshTokenService.createRefreshToken(
                getUserByEmail, newRefreshToken).getToken();
        String token = jwtUtil.generateToken(getUserByEmail);
        return new TokenDto(token, refreshToken);
    }

    public TokenDto generateRefreshToken(@NonNull String refreshTokenCookie) {
        RefreshToken tokenFound = refreshTokenService.getByToken(refreshTokenCookie);
        if (refreshTokenService.isExpired(tokenFound)) {
            refreshTokenService.deleteRefreshToken(tokenFound);
            throw new RefreshTokenExpireException("Refresh token is expired, you need to login again");
        }
        UserDetails userDetails = (UserDetails) tokenFound.getUser();
        String newJwt = jwtUtil.generateToken(tokenFound.getUser());
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
        tokenFound.setToken(newRefreshToken);
        tokenFound.setExpiredAt(LocalDateTime.now().plusSeconds(jwtRefreshTokenExpirationMs));
        refreshTokenService.saveRefreshToken(tokenFound);
        return new TokenDto(newJwt, newRefreshToken);
    }

    public void userLogout(@NonNull String refreshToken) {
        RefreshToken tokenFound = refreshTokenService.getByToken(refreshToken);
        refreshTokenService.deleteRefreshToken(tokenFound);
    }
}
