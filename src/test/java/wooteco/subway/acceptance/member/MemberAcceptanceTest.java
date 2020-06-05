package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.web.ExceptionAdvice.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class MemberAcceptanceTest extends AcceptanceTest {

	@DisplayName("회원 정보 관리")
	@Test
	void memberInfo() {
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
		final int statusCode = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		assertThat(statusCode).isEqualTo(204);

		TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		assertThat(tokenResponse.getAccessToken()).isNotBlank();

		updateMemberWhenLoggedIn(tokenResponse.getAccessToken(), "NEW_NAME",
			"NEW_PASSWORD");

		MemberResponse memberResponseAfterEdit = getMember(TEST_USER_EMAIL, tokenResponse);
		assertThat(memberResponseAfterEdit).isNotNull();
		assertThat(memberResponseAfterEdit.getName()).isEqualTo("NEW_NAME");
		assertThat(memberResponseAfterEdit.getId()).isNotNull();
		assertThat(memberResponseAfterEdit.getEmail()).isEqualTo(TEST_USER_EMAIL);

		deleteMember(tokenResponse);
	}

	@DisplayName("회원 정보 관리 중 없는 이메일 계정으로 로그인 시도 예외")
	@Test
	void memberInfoSideCases() {
		/**
		 * When 없는 이메일로 로그인을 시도한다
		 * Then Exception이 발생한다
		 */
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("email", TEST_USER_EMAIL);
		paramMap.put("password", TEST_USER_PASSWORD);

		String message = given()
			.body(paramMap)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/me/login")
			.then()
			.log().all()
			.extract()
			.asString();
		assertThat(message).contains(BAD_REQUEST_MESSAGE);
	}

	@DisplayName("회원 정보 관리 중 동일 이메일로 회원가입 요청 예외")
	@Test
	void duplicateEmail() {
		/**
		 * When 동일한 이메일로 회원가입을 요청한다
		 * Then Exception이 발생한다
		 */
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Map<String, String> params = new HashMap<>();
		params.put("email", TEST_USER_EMAIL);
		params.put("name", "Allen");
		params.put("password", "1234");

		String message =
			given().
				body(params).
				contentType(MediaType.APPLICATION_JSON_VALUE).
				accept(MediaType.APPLICATION_JSON_VALUE).
				when().
				post("/me").
				then().
				log().all().
				extract().asString();
		assertThat(message).contains(BAD_REQUEST_MESSAGE);
	}

	@DisplayName("회원 정보 관리 중 맞지 않는 비밀번호로 로그인 요청 예외")
	@Test
	void wrongPassword() {
		/**
		 * When 맞지 않는 비밀번호로 로그인을 시도한다
		 * Then Exception이 발생한다
		 */
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("email", TEST_USER_EMAIL);
		paramMap.put("password", "CU");

		String message =
			given()
				.body(paramMap)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/me/login")
				.then()
				.log().all()
				.extract().asString();
		assertThat(message).contains(BAD_REQUEST_MESSAGE);
	}

	@DisplayName("회원 정보 관리 중 유효하지 않은 토큰으로 권한이 필요한 페이지 요청 예외")
	@Test
	void invalidToken() {
		/**
		 * When 유효하지 않은 토큰으로 권한을 요하는 페이지를 요청한다
		 * Then Exception이 발생한다
		 */
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		TokenResponse tokenResponse = new TokenResponse("BrownToken", "Bearer");

		String message = given()
			.header("Authorization",
				tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/me")
			.then()
			.log().all()
			.extract().asString();
		assertThat(message).contains("유효하지 않은 토큰");
	}
}
