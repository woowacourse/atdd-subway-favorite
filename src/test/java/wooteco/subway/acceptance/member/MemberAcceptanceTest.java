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

	@DisplayName("회원 정보 관리")
	@Test
	void memberInfo() {
		String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		assertThat(location).isNotBlank();

		TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		assertThat(tokenResponse.getAccessToken()).isNotBlank();

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
		 *
		 * Given 회원가입이 되어있고, 로그인이 되어있다.
		 * When 회원정보 삭제 요청을 한다.
		 * Then 회원정보가 삭제된다.
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

}
