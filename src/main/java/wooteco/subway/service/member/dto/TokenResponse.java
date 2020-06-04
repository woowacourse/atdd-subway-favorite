package wooteco.subway.service.member.dto;

public class TokenResponse {
    private String accessToken;
    private String tokenType;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public static TokenResponse of(String token) {
        String[] tokenInfo = token.split(" ");
        return new TokenResponse(tokenInfo[1], tokenInfo[0]);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
