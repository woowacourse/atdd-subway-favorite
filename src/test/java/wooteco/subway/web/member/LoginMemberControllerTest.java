package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.AcceptanceTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.google.gson.Gson;
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.web.advice.dto.ErrorResponse;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	private Gson gson;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.apply(documentationConfiguration(restDocumentation))
			.build();

		gson = new Gson();
	}

	@DisplayName("로그인 실패 테스트")
	@Test
	public void login() throws Exception {
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		TokenResponse tokenResponse = new TokenResponse(
			"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicm93bkBlbWFpbC5jb20iLCJpYXQiOjE1ODk3OTY3MjMsImV4cCI6MTU4OTgwMDMyM30.TuYskGqrntZz6sx6Gzr3bO8SHnR2pb99aSEAhJyaKpU",
			"bearer");

		given(memberService.createToken(any())).willReturn(
			tokenResponse.getAccessToken());

		this.mockMvc.perform(post("/oauth/token")
			.content(gson.toJson(loginRequest))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(gson.toJson(tokenResponse)))
			.andDo(print())
			.andDo(LoginMemberDocumentation.login());
	}

	@DisplayName("로그인 실패 테스트")
	@Test
	public void loginFail() throws Exception {
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
		ErrorResponse errorResponse = ErrorResponse.of(new IllegalArgumentException());

		given(memberService.createToken(any())).willThrow(
			new IllegalArgumentException("잘못된 패스워드입니다."));

		this.mockMvc.perform(post("/oauth/token")
			.content(gson.toJson(loginRequest))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().json(gson.toJson(errorResponse)))
			.andDo(print())
			.andDo(LoginMemberDocumentation.loginFail());
	}
}
