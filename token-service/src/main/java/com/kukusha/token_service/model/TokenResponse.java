package com.kukusha.token_service.model;

import java.time.Instant;

public record TokenResponse(String token,
                            Instant expAt) {
}
