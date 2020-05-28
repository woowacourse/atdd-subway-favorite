package wooteco.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    // Feature: 인증
    //
    // Scenario: Bearer 인증을 한다.
    // Given 멤버가 생성되어있는 상태이다.
    //
    // When 로그인 요청을 한다.
    // Then 토큰을 받아온다.
    //
    // When 토큰으로 멤버 정보를 요청한다.
    // Then 멤버 정보를 받아온다.

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

    public MemberResponse myInfoWithBearerAuth(TokenResponse tokenResponse) {
        return given().log().all()
                .auth()
                .oauth2(tokenResponse.getAccessToken())
                .when()
                .get("/me/bearer")
                .then().log().all()
                .extract().as(MemberResponse.class);
    }
}
