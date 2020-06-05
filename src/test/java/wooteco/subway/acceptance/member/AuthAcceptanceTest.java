package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class AuthAcceptanceTest extends AcceptanceTest {
    @DisplayName("Bearer Login")
    @Test
    void validBearerLogin() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        assertThat(tokenResponse).isNotNull();
    }

    @DisplayName("invalid Bearer Login")
    @Test
    void invalidBearerLogin() {
        String result = loginWithInvalidAttributes(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        assertThat(result).contains("email이 존재하지 않습니다.");
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        MemberResponse memberResponse = myInfoWithBearerAuth(tokenResponse);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
    }
}
