package com.example.learnjava.springmvc.service;

import java.util.Optional;

import com.example.learnjava.springmvc.dto.Request.LoginRequestDTO;
import com.example.learnjava.springmvc.dto.Request.RegisterRequestDTO;
import com.example.learnjava.springmvc.dto.Response.AuthResponseDTO;
import com.example.learnjava.springmvc.dto.Response.RefreshTokenResponseDTO;
import com.example.learnjava.springmvc.model.RefreshToken;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);

    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);

    Optional<RefreshToken> getRefreshToken(String token);

    boolean deleteRefreshToken(String token);

    RefreshTokenResponseDTO refreshAccessToken(String refreshToken);
}
