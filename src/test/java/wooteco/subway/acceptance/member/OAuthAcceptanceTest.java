package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.TokenResponse;

public class OAuthAcceptanceTest extends AcceptanceTest {

	@DisplayName("로그인 기능")
	@Test
	void loginMember() {
		// given : 회원이 등록되어 있다.
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		// when : 로그인을 요청한다.
		TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		// then : 토큰을 응답받는다.
		assertThat(tokenResponse.getAccessToken()).isNotBlank();
		assertThat(tokenResponse.getTokenType()).isEqualTo("Bearer");
	}
}
