package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.acceptance.member.MemberAcceptanceTest.TEST_USER_EMAIL;
import static wooteco.subway.acceptance.member.MemberAcceptanceTest.TEST_USER_NAME;
import static wooteco.subway.acceptance.member.MemberAcceptanceTest.TEST_USER_PASSWORD;
import static wooteco.subway.exception.InvalidMemberException.DUPLICATED_EMAIL;
import static wooteco.subway.web.member.MemberController.MEMBER_URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.exception.InvalidMemberException;
import wooteco.subway.service.member.dto.JoinRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.web.ControllerTest;

class MemberControllerTest extends ControllerTest {
	@Test
	void createMember() throws Exception {
		MemberResponse response = MemberResponse.of(member);
		given(memberService.createMember(any())).willReturn(response);
		JoinRequest joinRequest = new JoinRequest(TEST_USER_EMAIL, TEST_USER_NAME,
			TEST_USER_PASSWORD);

		String request = objectMapper.writeValueAsString(joinRequest);
		post(MEMBER_URI, request, status().isCreated(), MemberDocumentation.createMember());
	}

	@DisplayName("이미 가입된 이메일로 가입 요청 시 예외 발생")
	@Test
	void failedCreateMember() throws Exception {
		given(memberService.createMember(any())).willThrow(
			new InvalidMemberException(DUPLICATED_EMAIL, anyString()));
		JoinRequest joinRequest = new JoinRequest(TEST_USER_EMAIL, TEST_USER_NAME,
			TEST_USER_PASSWORD);

		String request = objectMapper.writeValueAsString(joinRequest);
		post(MEMBER_URI, request, status().isBadRequest(), MemberDocumentation.failedCreateMemberByDuplication());
	}

	@DisplayName("회원 가입 시 빈 문자열 입력이 들어올 경우 예외 발생")
	@Test
	void failedCreateMember2() throws Exception {
		JoinRequest joinRequest = new JoinRequest(TEST_USER_EMAIL, "", TEST_USER_PASSWORD);

		String request = objectMapper.writeValueAsString(joinRequest);
		postWithAuth(MEMBER_URI, request, status().isBadRequest(), MemberDocumentation.failedCreateMemberByBlank());
	}
}
