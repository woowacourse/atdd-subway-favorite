package wooteco.subway.infra;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import wooteco.subway.exception.InvalidAuthenticationException;

class JwtTokenProviderTest {
    TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider("key", 36000000);
    }

    @DisplayName("null/빈값의 토큰으로 부터 value를 얻으려는 경우 InvalidAuthenticationException 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void getSubject(String token) {
        assertThatThrownBy(() -> tokenProvider.getSubject(token)).
            isInstanceOf(InvalidAuthenticationException.class);
    }
}