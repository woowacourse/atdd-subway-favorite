package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class LoginMemberAcceptanceTest extends AcceptanceTest {

	@DisplayName("로그인 기능")
	@Test
	void loginMember() {
		// given : 회원이 등록되어 있다.
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		// when : 로그인을 요청한다.
		TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		// then : 토큰을 응답받는다.
		assertThat(tokenResponse.getAccessToken()).isNotBlank();
		assertThat(tokenResponse.getTokenType()).isEqualTo("bearer");

		// when : 회원정보를 요청한다.
		MemberResponse memberResponse = findMemberBy(tokenResponse);

		// then : 회원정보를 응답받는다.
		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
	}

	private TokenResponse login(String email, String password) {
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		params.put("password", password);

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

	private MemberResponse findMemberBy(TokenResponse tokenResponse) {
		return given().
			auth().oauth2(tokenResponse.getAccessToken()).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			get("/oauth/member").
			then().
			log().all().
			statusCode(HttpStatus.OK.value()).
			extract().as(MemberResponse.class);
	}
}
