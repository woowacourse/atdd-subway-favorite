package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AcceptanceTest {
    @DisplayName("로그인")
    @Test
    void login() {
        // given
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        // when
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
        assertThat(tokenResponse.getTokenType()).isNotBlank();
    }
}
