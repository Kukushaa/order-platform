package com.kukusha.auth_service.controller;

import com.kukusha.auth_service.db.service.UsersService;
import com.kukusha.auth_service.dto.LoginRequestDTO;
import com.kukusha.auth_service.dto.RegisterRequestDTO;
import com.kukusha.auth_service.exceptions.UsernameExistsException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    private final JwtEncoder jwtEncoder;
    private final UsersService usersService;
    private final RSAKey rsaJwk;

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, UsersService usersService, RSAKey rsaJwk) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.usersService = usersService;
        this.rsaJwk = rsaJwk;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.username();

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginRequestDTO.password())
        );

        Instant now = Instant.now();
        Instant exp = now.plus(minutes, ChronoUnit.MINUTES);

        List<String> userRoles = authenticate.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(exp)
                .subject(authenticate.getName())
                .claim("role", userRoles)
                .claim("username", username)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256)
                .keyId(rsaJwk.getKeyID())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        return new ResponseEntity<>(Map.of(
                "accessToken", token,
                "tokenType", "Bearer",
                "expAt", exp.toString()),
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
        return new ResponseEntity<>(new JWKSet(rsaJwk.toPublicJWK()).toJSONObject(), HttpStatus.OK);
    }
}
