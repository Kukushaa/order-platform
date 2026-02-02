package com.kukusha.auth_service.response;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType) {
}
