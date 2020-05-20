package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AcceptanceTest {
    @DisplayName("로그인 기능")
    @Test
    void manageMember() {
        // Given 유저 정보가 생성 되어 있다.
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        // When 로그인을 요청한다.
        // Then 토큰을 획득한다.
        TokenResponse tokenResponse = login();

        assertThat(tokenResponse.getTokenType()).isEqualTo("bearer");
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }

    private TokenResponse login() {
        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_USER_EMAIL);
        params.put("password", TEST_USER_PASSWORD);

        return
                given().
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        post("/oauth/token").
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(TokenResponse.class);
    }
}
