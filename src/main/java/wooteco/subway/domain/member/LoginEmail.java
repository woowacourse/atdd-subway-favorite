package wooteco.subway.domain.member;

import wooteco.subway.web.dto.ErrorCode;
import wooteco.subway.web.member.auth.InvalidAuthenticationException;

public class LoginEmail {
    private final String email;

    public LoginEmail(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void validate(final String email) {
        if (!this.email.equals(email)) {
            throw new InvalidAuthenticationException(ErrorCode.NOT_AUTHRIZED_EMAIL);
        }
    }
}