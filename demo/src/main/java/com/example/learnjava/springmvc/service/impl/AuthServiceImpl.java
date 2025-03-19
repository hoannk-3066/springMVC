package com.example.learnjava.springmvc.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.learnjava.springmvc.dto.Request.LoginRequestDTO;
import com.example.learnjava.springmvc.dto.Request.RegisterRequestDTO;
import com.example.learnjava.springmvc.dto.Response.AuthResponseDTO;
import com.example.learnjava.springmvc.dto.Response.RefreshTokenResponseDTO;
import com.example.learnjava.springmvc.dto.Response.UserResponseDTO;
import com.example.learnjava.springmvc.model.RefreshToken;
import com.example.learnjava.springmvc.model.User;
import com.example.learnjava.springmvc.repository.RefreshTokenRepository;
import com.example.learnjava.springmvc.repository.UserRepository;
import com.example.learnjava.springmvc.service.AuthService;
import com.example.learnjava.springmvc.utils.JwtConstants;
import com.example.learnjava.springmvc.utils.JwtUtil;
import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
            RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setName(registerRequestDTO.getName());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setAge(registerRequestDTO.getAge());
        userRepository.save(user);

        String token = jwtUtil.generateAccessToken(user.getEmail());
        RefreshToken refreshToken = createRefreshToken(user.getEmail());
        return AuthResponseDTO.builder().accessToken(token).refreshToken(refreshToken.getToken())
                .user(UserResponseDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .age(user.getAge())
                        .name(user.getName())
                        .build())
                .build();

    }

    @Override
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Sai email hoặc mật khẩu"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai email hoặc mật khẩu");
        }

        String token = jwtUtil.generateAccessToken(user.getEmail());
        RefreshToken refreshToken = createRefreshToken(user.getEmail());
        return AuthResponseDTO.builder().accessToken(token).refreshToken(refreshToken.getToken())
                .user(UserResponseDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .age(user.getAge())
                        .name(user.getName())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public Optional<RefreshToken> getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    @Override
    public boolean deleteRefreshToken(String token) {
        String email = jwtUtil.extractEmail(token);
        return refreshTokenRepository.deleteByEmail(email) > 0;
    }

    @Override
    public RefreshTokenResponseDTO refreshAccessToken(String refreshToken) {
        Optional<RefreshToken> tokenOpt = getRefreshToken(refreshToken);
        System.out.println("tokenOpt: " + tokenOpt);
        if (tokenOpt.isEmpty() || isRefreshTokenExpired(tokenOpt.get())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }
        if (refreshTokenRepository.deleteByEmail(tokenOpt.get().getEmail()) <= 0) {
            throw new RuntimeException("Invalid or expired refresh token");
        }
        RefreshToken newRefreshToken = createRefreshToken(tokenOpt.get().getEmail());
        String newAccessToken = jwtUtil.generateAccessToken(tokenOpt.get().getEmail());

        return RefreshTokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    private RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setEmail(email);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(JwtConstants.REFRESH_TOKEN_EXPIRATION));
        return refreshTokenRepository.save(refreshToken);
    }

    private boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }
}
