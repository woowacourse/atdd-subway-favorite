package wooteco.subway.acceptance;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

	@DisplayName("Session와 Bearer 방식으로 인증")
	@Test
	void AuthorizeSessionAndBearer() {
		String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		String id = location.split("/")[2];
		Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		String sessionId = response.getSessionId();

		TokenResponse tokenResponse = getTokenResponse(response);

		MemberResponse memberResponse = myInfoAuth(tokenResponse, sessionId, id);

		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
	}


	private MemberResponse myInfoAuth(TokenResponse tokenResponse, String sessionId, String memberId) {
		return given().
				cookie("JSESSIONID", sessionId).
				header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
				accept(MediaType.APPLICATION_JSON_VALUE).
				when().
				get("/members/" + memberId).
				then().
				log().all().
				statusCode(HttpStatus.OK.value()).
				extract().as(MemberResponse.class);
	}
}
