package com.kukusha.auth_service.controller;

import com.kukusha.auth_service.db.service.UsersService;
import com.kukusha.auth_service.dto.LoginRequestDTO;
import com.kukusha.auth_service.dto.RegisterRequestDTO;
import com.kukusha.auth_service.exceptions.UsernameExistsException;
import com.kukusha.auth_service.response.LoginResponse;
import com.kukusha.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UsersService usersService;

    public AuthController(UsersService usersService, AuthService authService) {
        this.usersService = usersService;
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponse loginResponse = authService.loginUser(loginRequestDTO.username(), loginRequestDTO.password());

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) throws UsernameExistsException {
        if (usersService.existsByUsername(registerRequestDTO.username())) {
            throw new UsernameExistsException("User with this username already exists");
        }

        usersService.register(registerRequestDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/jwks")
    public ResponseEntity<Map<String, Object>> getJwks() {
        return new ResponseEntity<>(authService.getPublicJWKAsJsonObject(), HttpStatus.OK);
    }
}
