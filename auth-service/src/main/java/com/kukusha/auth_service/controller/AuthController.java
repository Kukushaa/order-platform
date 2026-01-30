package com.kukusha.auth_service.controller;

import com.kukusha.auth_service.db.service.UsersService;
import com.kukusha.auth_service.dto.LoginRequestDTO;
import com.kukusha.auth_service.dto.RegisterRequestDTO;
import com.kukusha.auth_service.exceptions.UsernameExistsException;
import com.kukusha.token_service.img.AccessTokenDriver;
import com.kukusha.token_service.model.TokenCreateDTO;
import com.kukusha.token_service.model.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${token.issuer}")
    private String issuer;

    @Value("${token.minutes}")
    private int minutes;

    private final AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final AccessTokenDriver accessTokenDriver;

    public AuthController(AuthenticationManager authenticationManager, UsersService usersService, AccessTokenDriver accessTokenDriver) {
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
        this.accessTokenDriver = accessTokenDriver;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.username();

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginRequestDTO.password())
        );

        List<String> userRoles = authenticate.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        TokenResponse tokenResponse = accessTokenDriver.createToken(new TokenCreateDTO.TokenCreateDTOBuilder()
                .expAt(minutes)
                .chronoUnit(ChronoUnit.MINUTES)
                .issuer(issuer)
                .subject(authenticate.getName())
                .claims(Map.of(
                                "role", userRoles,
                                "username", username
                        )
                )
                .build());

        return new ResponseEntity<>(Map.of(
                "accessToken", tokenResponse.token(),
                "tokenType", "Bearer",
                "expAt", tokenResponse.expAt().toString()),
                HttpStatus.OK);
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
//
//        return new ResponseEntity<>(new JWKSet(rsaJwk.toPublicJWK()).toJSONObject(), HttpStatus.OK);
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
    }
}
