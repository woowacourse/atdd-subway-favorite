package wooteco.subway.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

import static org.assertj.core.api.Assertions.assertThat;

@TestComponent
class JwtTokenProviderTest {
    private static final String SECRET_KEY = "ksljflkdjklfsdlkwqeifj";
    private static final int VALIDITY_INMILLISECONDS = 3000;

    @DisplayName("토큰 생성")
    @Test
    void createToken() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, VALIDITY_INMILLISECONDS);
        String token = jwtTokenProvider.createToken("테스트");
        String[] tokenUnit = token.split("\\.");

        assertThat(token).isNotNull();
        assertThat(tokenUnit).hasSize(3);
    }

    @DisplayName("Payload 값 가져오기")
    @Test
    void getSubject() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, VALIDITY_INMILLISECONDS);
        String expected = "테스트";
        String token = jwtTokenProvider.createToken(expected);

        String actual = jwtTokenProvider.getSubject(token);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("토큰 검증")
    @Test
    void validateToken() {
        // given
        // when
        // then
    }
}