package wooteco.subway.domain.Token;

public enum TokenType {
    BEARER("bearer");

    private final String tokenName;

    TokenType(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenName() {
        return tokenName;
    }
}
