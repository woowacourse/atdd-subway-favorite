package wooteco.subway.domain.member;

public class LoginEmail {
    private final String email;

    public LoginEmail(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}