package com.kukusha.token_service.model;

import java.time.temporal.ChronoUnit;
import java.util.Map;

public record TokenCreateDTO(String subject,
                             String issuer,
                             int expAt,
                             ChronoUnit chronoUnit,
                             Map<String, Object> claims) {

    public static class TokenCreateDTOBuilder {
        private String subject;
        private String issuer;
        private int expAt;
        private ChronoUnit chronoUnit;
        private Map<String, Object> claims;

        public TokenCreateDTOBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public TokenCreateDTOBuilder issuer(String issuer) {
            this.issuer = issuer;

            return this;
        }

        public TokenCreateDTOBuilder expAt(int expAt) {
            this.expAt = expAt;

            return this;
        }

        public TokenCreateDTOBuilder chronoUnit(ChronoUnit chronoUnit) {
            this.chronoUnit = chronoUnit;

            return this;
        }

        public TokenCreateDTOBuilder claims(Map<String, Object> claims) {
            this.claims = claims;

            return this;
        }

        public TokenCreateDTO build() {
            return new TokenCreateDTO(subject, issuer, expAt, chronoUnit, claims);
        }
    }
}
