package com.kukusha.users_shared_lib.dto;

public record RegisterRequestDTO(String name,
                                 String surname,
                                 String email,
                                 String username,
                                 String password) {
}
