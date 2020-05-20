package wooteco.subway;

import io.restassured.authentication.FormAuthConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

	@DisplayName("Session")
	@Test
	void myInfoWithSession() {
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		MemberResponse memberResponse = myInfoWithSession(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
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

	public MemberResponse myInfoWithSession(String email, String password) {
		return given().auth().
				form(email, password, new FormAuthConfig("/login", "email", "password")).
				when().
				get("/me/session").
				then().
				log().
				all().statusCode(HttpStatus.OK.value()).
				extract().as(MemberResponse.class);
	}

	public MemberResponse myInfoWithBearerAuth(TokenResponse tokenResponse) {
		return given().auth().
				oauth2(tokenResponse.getAccessToken()).
				accept(MediaType.APPLICATION_JSON_VALUE).
				when().
				get("/me/bearer").
				then().
				log().all().
				statusCode(HttpStatus.OK.value()).
				extract().as(MemberResponse.class);
	}

	public TokenResponse login(String email, String password) {
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
}
