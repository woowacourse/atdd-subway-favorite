package wooteco.subway.web.member.info;

import org.springframework.mock.web.MockHttpSession;

public class AuthInfo {
    private String token;
    private MockHttpSession session;

    private AuthInfo(String token, MockHttpSession session) {
        this.token = token;
        this.session = session;
    }

    public static AuthInfo of(String token, MockHttpSession session) {
        return new AuthInfo(token, session);
    }

    public static AuthInfo empty() {
        return new AuthInfo("", new MockHttpSession());
    }

    public String getToken() {
        return token;
    }

    public MockHttpSession getSession() {
        return session;
    }
}
