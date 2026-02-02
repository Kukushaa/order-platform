package com.kukusha.auth_service.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType) {
}
