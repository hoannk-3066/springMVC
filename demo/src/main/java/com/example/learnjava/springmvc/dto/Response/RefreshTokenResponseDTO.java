package com.example.learnjava.springmvc.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RefreshTokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
