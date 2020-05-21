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

	@DisplayName("회원 정보 관리")
	@Test
	void memberInfo() {
		String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		assertThat(location).isNotBlank();

		TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		assertThat(tokenResponse.getAccessToken()).isNotBlank();

		MemberResponse memberResponse = getMember(TEST_USER_EMAIL, tokenResponse);
		updateMemberWhenLoggedIn(memberResponse, tokenResponse.getAccessToken(), "NEW_NAME", "NEW_PASSWORD");

		MemberResponse memberResponseAfterEdit = getMember(TEST_USER_EMAIL, tokenResponse);
		assertThat(memberResponseAfterEdit).isNotNull();
		assertThat(memberResponseAfterEdit.getName()).isEqualTo("NEW_NAME");
		assertThat(memberResponseAfterEdit.getId()).isNotNull();
		assertThat(memberResponseAfterEdit.getEmail()).isEqualTo(TEST_USER_EMAIL);

		deleteMember(memberResponseAfterEdit, tokenResponse);

		/**
		 * Feature: 회원 정보 관리
		 *
		 * Scenario: 회원 정보를 관리한다.
		 * When 회원가입 요청을 한다.
		 * Then 회원가입이 된다.
		 *
		 * Given 회원가입이 되어있고, 로그인이 되어있지 않다.
		 * When 로그인을 요청한다
		 * Then 로그인이 된다.
		 *
		 * Given 회원가입이 되어있고, 로그인이 되어있다.
		 * When 회원정보 조회 요청을 한다.
		 * Then 회원정보가 조회된다.
		 *
		 * Given 회원가입이 되어있고, 로그인이 되어있다.
		 * When 회원정보 수정 요청을 한다.
		 * Then 회원정보가 수정된다.
		 *  And 변경된 사항을 확인한다.
		 *
		 * Given 회원가입이 되어있고, 로그인이 되어있다.
		 * When 회원정보 삭제 요청을 한다.
		 * Then 회원정보가 삭제된다.
		 *
		 */
	}

	@DisplayName("회원 정보 관리 중 예외 테스트")
	@Test
	void memberInfoSideCases() {
		final String string = loginWithStringOutput(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		System.out.println(string);
		System.out.println("##");
		// assertThat(string)
		// assertThatThrownBy(() -> login(TEST_USER_EMAIL, TEST_USER_PASSWORD)).isInstanceOf(AssertionError.class);

		/**
		 * When 없는 이메일로 로그인을 시도한다
		 * Then Exception이 발생한다
		 *
		 * When 동일한 이메일로 회원가입을 요청한다
		 * Then Exception이 발생한다
		 *
		 * When 맞지 않는 비밀번호로 로그인을 시도한다
		 * Then Exception이 발생한다
		 *
		 * When 유효하지 않은 토큰으로 권한을 요하는 페이지를 요청한다
		 * Then Exception이 발생한다
		 */
	}

	private TokenResponse login(String email, String password) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("email", email);
		paramMap.put("password", password);

		return
			given()
				.body(paramMap)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/oauth/token")
				.then()
				.log().all()
				.statusCode(HttpStatus.OK.value())
				.extract().as(TokenResponse.class);
	}

	private String loginWithStringOutput(String email, String password) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("email", email);
		paramMap.put("password", password);

		return
			given()
				.body(paramMap)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/oauth/token")
				.then()
				.log().all()
				.extract()
				.asString();
	}

	public void updateMemberWhenLoggedIn(MemberResponse memberResponse, String accessToken, String name,
		String password) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("password", password);

		given().header("Authorization", "Bearer " + accessToken);

		given()
			.header("Authorization", "Bearer " + accessToken)
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.put("/members/" + memberResponse.getId())
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value());
	}
}
