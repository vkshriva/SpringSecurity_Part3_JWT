package com.lala.jwt.service;

import com.lala.jwt.entity.RefreshToken;
import com.lala.jwt.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    public String createRefreshToken(String email) {
        refreshTokenRepository.deleteByEmail(email); // Revoke existing tokens
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmail(email);
        refreshToken.setToken(UUID.randomUUID().toString());
        long sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000L; // 7 days in milliseconds
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + sevenDaysInMillis));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElse(null);
        if (refreshToken==null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        Date currentDate = new Date();
        if (refreshToken.getExpiryDate().before(currentDate)) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token expired");
        }
        return refreshToken;
    }
}
