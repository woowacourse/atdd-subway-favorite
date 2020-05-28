package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.web.member.MemberControllerTest.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.doc.OAuthDocumentation;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(value = {OAuthController.class, AuthorizationExtractor.class, JwtTokenProvider
	.class})
class OAuthControllerTest {
	// @formatter:off

	@MockBean
	private MemberService memberService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.apply(documentationConfiguration(restDocumentation))
			.build();
	}

	@DisplayName("로그인 요청")
	@Test
	void login() throws Exception {
		HashMap<String, String> request = new HashMap<>();
		request.put("email", EMAIL);
		request.put("password", PASSWORD);
		String jsonRequest
			= OBJECT_MAPPER.writeValueAsString(request);

		when(memberService.createToken(any(LoginRequest.class))).thenReturn(TOKEN);

		mockMvc.perform(post("/oauth/token")
			.content(jsonRequest)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tokenType").value("bearer"))
			.andDo(OAuthDocumentation.login());
	}


	// @formatter:on

	@DisplayName("예외테스트: null혹은 빈 값의 이메일, 비밀번호로 로그인 하는 경우")
	@Test
	void login_withNullOrEmptyValues() throws Exception {
		//given
		HashMap<String, String> nullRequest = new HashMap<>();
		nullRequest.put("email", null);
		nullRequest.put("password", null);
		String nullJson = OBJECT_MAPPER.writeValueAsString(nullRequest);

		//when, then
		mockMvc.perform(post("/oauth/token")
			.contentType(MediaType.APPLICATION_JSON)
			.content(nullJson))
			.andDo(print())
			.andExpect(status().isBadRequest());

		//given
		HashMap<String, String> emptyRequest = new HashMap<>();
		emptyRequest.put("email", "");
		emptyRequest.put("password", "");
		String emptyJson = OBJECT_MAPPER.writeValueAsString(emptyRequest);

		//when, then
		mockMvc.perform(post("/oauth/token")
			.contentType(MediaType.APPLICATION_JSON)
			.content(emptyJson))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@DisplayName("예외테스트: 적절한 형식이 아닌 이메일로 로그인하는 경우")
	@Test
	void login_withInvalidEmail() throws Exception {
		//given
		HashMap<String, String> invalidRequest = new HashMap<>();
		invalidRequest.put("email", "chomily");
		invalidRequest.put("password", "1234");
		String invalidJson = OBJECT_MAPPER.writeValueAsString(invalidRequest);

		//when, then
		mockMvc.perform(post("/oauth/token")
			.contentType(MediaType.APPLICATION_JSON)
			.content(invalidJson))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
}
