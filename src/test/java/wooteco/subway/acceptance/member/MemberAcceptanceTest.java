package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class MemberAcceptanceTest extends AcceptanceTest {

	@DisplayName("회원 관리 기능")
	@Test
	void manageMember() {
		String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		assertThat(location).isNotBlank();

		MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

		updateMember(memberResponse);
		MemberResponse updatedMember = getMember(TEST_USER_EMAIL);
		assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

		deleteMember(memberResponse);
	}

	/*
	 * when 회원가입 요청을 한다.
	 * and 로그인 요청을 한다.
	 * then 회원 조회가 가능하다.
	 *
	 * when 회원 정보를 수정한다.
	 * then 회원 정보를 조회해서 수정 잘됐는지 확인한다.
	 *
	 * when 회원 탈퇴 한다.
	 * then 회원조회를 해서 탈퇴되었는지 확인한다.
	 * */

	@DisplayName("회원 자기 자신이 정보를 관리한다")
	@Test
	void manageMemberSelf() {
		String member = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		assertThat(member).isNotBlank();

		TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		MemberResponse memberResponse = myInfoWithBearerAuth(tokenResponse);

		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
		assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);

		updateInfoBearerAuth(tokenResponse);

		memberResponse = myInfoWithBearerAuth(tokenResponse);
		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getName()).isEqualTo("NEW_" + TEST_USER_NAME);
	}

	private void updateInfoBearerAuth(TokenResponse tokenResponse) {
		HashMap<String, String> params = new HashMap<>();
		params.put("name", "NEW_" + TEST_USER_NAME);
		params.put("password", "NEW_" + TEST_USER_PASSWORD);

		given()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth()
			.oauth2(tokenResponse.getAccessToken())
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.put("/me/bearer")
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value());
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

	public MemberResponse myInfoWithBearerAuth(TokenResponse tokenResponse) {
		return given().auth()
			.oauth2(tokenResponse.getAccessToken())
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/me/bearer")
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().as(MemberResponse.class);
	}
}
