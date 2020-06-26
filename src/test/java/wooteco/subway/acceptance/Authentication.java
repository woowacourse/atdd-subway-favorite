package wooteco.subway.acceptance;

import wooteco.subway.service.member.dto.TokenResponse;

public class Authentication {
    String sessionId;
    TokenResponse tokenResponse;

    public Authentication(String sessionId, TokenResponse tokenResponse) {
        this.sessionId = sessionId;
        this.tokenResponse = tokenResponse;
    }

    public static Authentication of(String sessionId, TokenResponse tokenResponse) {
        return new Authentication(sessionId, tokenResponse);
    }

    public String getSessionId() {
        return sessionId;
    }

    public TokenResponse getTokenResponse() {
        return tokenResponse;
    }
}
