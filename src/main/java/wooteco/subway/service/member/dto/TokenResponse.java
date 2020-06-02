package wooteco.subway.service.member.dto;

import wooteco.subway.domain.token.TokenType;

public class TokenResponse {
    private String accessToken;
    private String tokenType;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken, TokenType tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType.getTokenType();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
