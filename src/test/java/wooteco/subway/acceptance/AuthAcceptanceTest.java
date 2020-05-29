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

	@DisplayName("Session Bearer 둘다")
	@Test
	void AuthorizeSessionAndBearer() {
		// given 회원가입 된 유저가 있다
		String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		String id = location.split("/")[2];

		// and 유저가 로그인을 했다
		Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		String sessionId = response.getSessionId();
		TokenResponse tokenResponse = getTokenResponse(response);

		// when 유저가 자기 정보를 인증 정보를 이용하여 조회한다
		MemberResponse memberResponse = readMyInfoWithAuth(tokenResponse, sessionId, id);

		//then 자기 정보가 조회되었다
		assertThat(memberResponse.getId()).isEqualTo(Long.parseLong(id));
		assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
	}

	public MemberResponse readMyInfoWithAuth(TokenResponse tokenResponse, String sessionId, String memberId) {
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
