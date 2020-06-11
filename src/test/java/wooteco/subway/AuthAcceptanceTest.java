package wooteco.subway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class AuthAcceptanceTest extends AcceptanceTest {

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

	private MemberResponse myInfoWithBearerAuth(TokenResponse tokenResponse) {
		return given()
			.auth()
			.oauth2(tokenResponse.getAccessToken())
			.when()
			.get("/me/bearer")
			.then()
			.statusCode(HttpStatus.OK.value())
			.extract().as(MemberResponse.class);
	}
}
