package com.kukusha.auth_service.dto;

public record RegisterRequestDTO(String name, String surname, String email, String username, String password) {
}
