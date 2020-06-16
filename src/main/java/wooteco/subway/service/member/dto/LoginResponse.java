package wooteco.subway.service.member.dto;

public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private String email;

    public LoginResponse() {
    }

    public LoginResponse(String accessToken, String tokenType, String email) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getEmail() {
        return email;
    }
}
