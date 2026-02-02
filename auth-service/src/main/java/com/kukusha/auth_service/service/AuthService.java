package com.kukusha.auth_service.service;

import com.kukusha.auth_service.response.TokenResponse;
import com.kukusha.token_service.model.TokenCreateDTO;
import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.TokenProcessorDriver;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    private static final String TOKEN_TYPE = "Bearer";

    @Value("${token.issuer}")
    private String issuer;

    @Value("${token.access-minutes}")
    private int accessTokenMinutes;

    @Value("${token.refresh-days}")
    private int refreshTokenDays;

    private final AuthenticationManager authenticationManager;
    private final TokenProcessorDriver tokenProcessorDriver;

    public AuthService(AuthenticationManager authenticationManager, TokenProcessorDriver tokenProcessorDriver) {
        this.authenticationManager = authenticationManager;
        this.tokenProcessorDriver = tokenProcessorDriver;
    }

    public TokenResponse loginUser(String username, String password) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        List<String> userRoles = authenticate.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        TokensData tokensData = generateTokens(userRoles, username, authenticate.getName());

        return new TokenResponse(tokensData.accessToken.token(), tokensData.refreshToken.token(), TOKEN_TYPE);
    }

    public Map<String, Object> getPublicJWKAsJsonObject() {
        return new JWKSet(tokenProcessorDriver.getTokenProcessor(TokenType.ACCESS)
                .encObj()
                .getRsaJwk()
                .toPublicJWK())
                .toJSONObject();
    }

    public TokenResponse refreshTokens(String refreshToken) {
        Jwt jwt = tokenProcessorDriver.getTokenProcessor(TokenType.REFRESH)
                .encObj()
                .getJwtDecoder()
                .decode(refreshToken);

        String username = jwt.getClaimAsString("username");
        List<String> roles = jwt.getClaimAsStringList("role");

        TokensData generateTokens = generateTokens(roles, username, jwt.getSubject());

        return new TokenResponse(generateTokens.accessToken().token(), generateTokens.refreshToken().token(), TOKEN_TYPE);
    }

    private TokensData generateTokens(List<String> roles, String username, String subject) {
        TokenCreateDTO.TokenCreateDTOBuilder builder = new TokenCreateDTO.TokenCreateDTOBuilder()
                .issuer(issuer)
                .subject(subject)
                .claims(Map.of("role", roles, "username", username));

        com.kukusha.token_service.model.TokenResponse accessToken = tokenProcessorDriver.getTokenProcessor(TokenType.ACCESS)
                .createToken(builder
                        .expAt(accessTokenMinutes)
                        .chronoUnit(ChronoUnit.MINUTES)
                        .build());

        com.kukusha.token_service.model.TokenResponse newRefreshToken = tokenProcessorDriver.getTokenProcessor(TokenType.REFRESH)
                .createToken(builder
                        .expAt(refreshTokenDays)
                        .chronoUnit(ChronoUnit.DAYS)
                        .build());

        return new TokensData(accessToken, newRefreshToken);
    }

    private record TokensData(com.kukusha.token_service.model.TokenResponse accessToken,
                              com.kukusha.token_service.model.TokenResponse refreshToken) {

    }
}
