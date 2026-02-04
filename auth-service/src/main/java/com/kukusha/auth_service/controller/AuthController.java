package com.kukusha.auth_service.controller;

import com.kukusha.auth_service.db.service.UsersService;
import com.kukusha.auth_service.dto.LoginRequestDTO;
import com.kukusha.auth_service.dto.RefreshTokenRequestDTO;
import com.kukusha.auth_service.dto.RegisterRequestDTO;
import com.kukusha.auth_service.exceptions.UsernameExistsException;
import com.kukusha.auth_service.response.TokenResponse;
import com.kukusha.auth_service.service.AuthService;
import com.kukusha.kafka_messages_sender.api.KafkaMessagesSenderAPI;
import com.kukusha.kafka_messages_sender.model.EmailType;
import com.kukusha.kafka_messages_sender.model.RegisterSuccessfullEmailData;
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
    private final KafkaMessagesSenderAPI kafkaMessagesSenderAPI;

    public AuthController(UsersService usersService, AuthService authService, KafkaMessagesSenderAPI kafkaMessagesSenderAPI) {
        this.usersService = usersService;
        this.authService = authService;
        this.kafkaMessagesSenderAPI = kafkaMessagesSenderAPI;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        TokenResponse tokenResponse = authService.loginUser(loginRequestDTO.username(), loginRequestDTO.password());

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) throws UsernameExistsException {
        if (usersService.existsByUsername(registerRequestDTO.username())) {
            throw new UsernameExistsException("User with this username already exists");
        }

        usersService.register(registerRequestDTO);
        kafkaMessagesSenderAPI.sendEmail(EmailType.REGISTER_USER, new RegisterSuccessfullEmailData(registerRequestDTO.email(), registerRequestDTO.username()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        TokenResponse tokenResponse = authService.refreshTokens(refreshTokenRequestDTO.refreshToken());
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/jwks")
    public ResponseEntity<Map<String, Object>> getJwks() {
        return new ResponseEntity<>(authService.getPublicJWKAsJsonObject(), HttpStatus.OK);
    }
}
