package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.web.member.MemberControllerTest.*;

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
		LoginRequest request = new LoginRequest(EMAIL, PASSWORD);
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
}