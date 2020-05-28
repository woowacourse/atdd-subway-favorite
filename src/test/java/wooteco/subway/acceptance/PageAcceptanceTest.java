package wooteco.subway.acceptance;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.TokenResponse;

public class PageAcceptanceTest extends AcceptanceTest {
	@LocalServerPort
	int port;

	// Feature: 페이지 조회
	//
	// Scenario: 페이지를 조회한다.
	//
	// When 회원가입 페이지를 조회한다.
	// Then 회원가입 페이지가 조회된다.
	//
	// When 로그인 페이지를 조회한다.
	// Then 로그인 페이지가 조회된다.
	//
	// Given 로그인이 되어있지 않다.
	// When 마이페이지를 조회한다.
	// Then 401 상태를 반환한다.
	//
	// Given 로그인이 되어있다.
	// When 마이페이지를 조회한다.
	// Then 마이페이지가 조회된다.
	//
	// Given 로그인이 되어있지 않다.
	// When 나의 정보 수정 페이지를 조회한다.
	// Then 401 상태를 반환한다.
	//
	// Given 로그인이 되어있다.
	// When 마이페이지 - 수정 페이지를 조회한다.
	// Then 마이페이지 - 수정 페이지가 조회된다.
	//
	// Given 로그인이 되어있지 않다.
	// When 즐겨찾기 페이지를 조회한다.
	// Then 401 상태를 반환한다.
	//
	// Given 로그인이 되어있다.
	// When 즐겨찾기 페이지를 조회한다.
	// Then 즐겨찾기 페이지가 조회된다.

	@DisplayName("페이지 인수 테스트")
	@Test
	void page() {
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		given().log().all().
			accept(MediaType.TEXT_HTML_VALUE).
			when().
			get("/join").
			then().log().all().
			statusCode(HttpStatus.OK.value()).
			body("html.head.title", containsString("회원가입"));

		given().log().all().
			accept(MediaType.TEXT_HTML_VALUE).
			when().
			get("/login").
			then().log().all().
			statusCode(HttpStatus.OK.value()).
			body(TITLE_PATH, containsString("로그인"));

		given().log().all().
			accept(MediaType.TEXT_HTML_VALUE).
			when().
			get("/mypage").
			then().log().all().
			statusCode(HttpStatus.UNAUTHORIZED.value());

		given().log().all().
			auth().
			oauth2(tokenResponse.getAccessToken()).
			accept(MediaType.TEXT_HTML_VALUE).
			when().
			get("/mypage").
			then().log().all().
			statusCode(HttpStatus.OK.value()).
			body(TITLE_PATH, containsString("마이페이지"));

		given().log().all().
			accept(MediaType.TEXT_HTML_VALUE).
			when().
			get("/mypage-edit").
			then().log().all().
			statusCode(HttpStatus.UNAUTHORIZED.value());

		given().log().all().
			auth().
			oauth2(tokenResponse.getAccessToken()).
			accept(MediaType.TEXT_HTML_VALUE).
			when().
			get("/mypage-edit").
			then().log().all().
			statusCode(HttpStatus.OK.value()).
			body(TITLE_PATH, containsString("마이페이지 수정"));

		given().log().all().
			accept(MediaType.TEXT_HTML_VALUE).
			when().
			get("/favorites-page").
			then().log().all().
			statusCode(HttpStatus.UNAUTHORIZED.value());

		given().log().all().
			auth().
			oauth2(tokenResponse.getAccessToken()).
			accept(MediaType.TEXT_HTML_VALUE).
			when().
			get("/favorites-page").
			then().log().all().
			statusCode(HttpStatus.OK.value()).
			body(TITLE_PATH, containsString("즐겨찾기"));
	}
}
