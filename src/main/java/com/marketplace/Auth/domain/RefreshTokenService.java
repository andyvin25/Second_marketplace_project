package com.marketplace.Auth.domain;

import com.marketplace.Exception.ResourceNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.token}")
    private String jwtRefreshSecret;
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Transactional(readOnly = true)
    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
    }

    @Transactional
    public RefreshToken createRefreshToken(User user, String newRefreshToken) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .expiredAt(LocalDateTime.now().plusDays(2))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public void saveRefreshToken(RefreshToken refreshToken) {
        Objects.requireNonNull(refreshToken);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeByUserId(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public void purgeExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredBefore(LocalDateTime.now());
    }

    public void deleteRefreshToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    public boolean isExpired(RefreshToken token) {
        return token.getExpiredAt().isBefore(LocalDateTime.now());
    }
}