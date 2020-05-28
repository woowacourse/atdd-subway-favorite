package wooteco.subway.service.member.dto;

public class TokenResponse {
    private String accessToken;
    private String tokenType;

    private TokenResponse() {
    }

    public TokenResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
