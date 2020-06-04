package wooteco.subway.web.member;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.acceptance.member.MemberAcceptanceTest.TEST_USER_EMAIL;
import static wooteco.subway.acceptance.member.MemberAcceptanceTest.TEST_USER_NAME;
import static wooteco.subway.acceptance.member.MemberAcceptanceTest.TEST_USER_PASSWORD;
import static wooteco.subway.web.member.LoginMemberController.MEMBER_URI_WITH_AUTH;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.ControllerTest;

class LoginMemberControllerTest extends ControllerTest {
	@Test
	void login() throws Exception {
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		String request = objectMapper.writeValueAsString(loginRequest);
		post(MEMBER_URI_WITH_AUTH, request, status().isOk(), MemberDocumentation.loginMember());
	}

	@Test
	void getMemberByEmail() throws Exception {
		given(memberService.findMemberByEmail(TEST_USER_EMAIL)).willReturn(member);

		MvcResult result = getWithAuth(MEMBER_URI_WITH_AUTH, MemberDocumentation.getMemberByEmail());

		ObjectMapper mapper = new ObjectMapper();
		mapper.readValue(result.getResponse().getContentAsString(), MemberResponse.class);
	}

	@Test
	void updateMember() throws Exception {
		UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest(TEST_USER_NAME,
			TEST_USER_PASSWORD);

		String request = objectMapper.writeValueAsString(updateMemberRequest);

		putWithAuth(MEMBER_URI_WITH_AUTH, request, status().isOk());
	}

	@Test
	void deleteMember() throws Exception {
		deleteWithAuth(MEMBER_URI_WITH_AUTH, MemberDocumentation.deleteMember());
	}

	@DisplayName("정보 수정 시 빈 문자열 입력이 들어올 경우 예외 발생")
	@Test
	public void failedUpdateMember() throws Exception {
		UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("",
			TEST_USER_PASSWORD);

		String request = objectMapper.writeValueAsString(updateMemberRequest);

		putWithAuthThenFail(MEMBER_URI_WITH_AUTH, request);
	}
}
