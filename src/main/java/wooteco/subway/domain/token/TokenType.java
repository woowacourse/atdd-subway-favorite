package wooteco.subway.domain.token;

public enum TokenType {
    BEARER("bearer");

    private String tokenType;

    TokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int length() {
        return BEARER.getTokenType().length();
    }

    public String getTokenType() {
        return tokenType;
    }
}
