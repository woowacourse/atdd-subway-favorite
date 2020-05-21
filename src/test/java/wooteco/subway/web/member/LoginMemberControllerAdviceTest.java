package wooteco.subway.web.member;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;

@Import(HttpEncodingAutoConfiguration.class)
@WebMvcTest(value = {LoginMemberController.class, AuthorizationExtractor.class, JwtTokenProvider
	.class})
public class LoginMemberControllerAdviceTest {

	@MockBean
	private MemberService memberService;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	private String email = "chomily@woowahan.com";
	private String password = "chomily1234";
	private String token = "You are authorized!";

	@DisplayName("해당 이메일을 가진 회원이 등록되어 있지 않을 경우")
	@Test
	void login_nonExistentEMail() throws Exception {
		LoginRequest loginRequest = new LoginRequest(email, password);
		String request = mapper.writeValueAsString(loginRequest);

		when(memberService.createToken(any(LoginRequest.class))).thenThrow(new RuntimeException("해당 이메일이 존재하지 않습니다."));

		mockMvc.perform(post("/oauth/token")
			.content(request)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().string("해당 이메일이 존재하지 않습니다."));
	}

	@DisplayName("패스워드가 일치하지 않는 경우")
	@Test
	void login_wrongPassword() throws Exception {
		LoginRequest loginRequest = new LoginRequest(email, password);
		String request = mapper.writeValueAsString(loginRequest);

		when(memberService.createToken(any(LoginRequest.class))).thenThrow(new RuntimeException("패스워드가 일치하지 않습니다."));

		mockMvc.perform(post("/oauth/token")
			.content(request)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().string("패스워드가 일치하지 않습니다."));
	}

	@DisplayName("비정상적인 로그인인 경우")
	@Test
	void login_abnormalLogin() throws Exception {
		when(jwtTokenProvider.getSubject(anyString())).thenReturn(email);
		when(memberService.findMemberByEmail(email)).thenThrow(
			new InvalidAuthenticationException("비정상적인 로그인입니다.")
		);

		mockMvc.perform(get("/oauth/member")
			.header("Authorization", "bearer " + token)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().string("비정상적인 로그인입니다."));
	}
}
