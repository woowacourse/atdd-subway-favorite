package wooteco.subway.web.member;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(value = {LoginMemberController.class, AuthorizationExtractor.class, JwtTokenProvider
	.class})
class LoginMemberControllerTest {

	@MockBean
	private MemberService memberService;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	private String email = "chomily@woowahan.com";
	private String name = "chomily";
	private String password = "chomily1234";
	private Long id = 1L;
	private String token = "You are authorized!";

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
		LoginRequest request = new LoginRequest(email, password);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonRequest
			= objectMapper.writeValueAsString(request);

		when(memberService.createToken(any(LoginRequest.class))).thenReturn(token);

		mockMvc.perform(post("/oauth/token")
			.content(jsonRequest)
			.contentType(MediaType.APPLICATION_JSON + "; charset=UTF-8")
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tokenType").value("bearer"))
			.andDo(LoginMemberDocumentation.login());
	}

	@DisplayName("토큰을 이용한 회원 정보 응답")
	@Test
	void getAuthorizedMember() throws Exception {
		when(jwtTokenProvider.getSubject(anyString())).thenReturn(email);
		when(memberService.findMemberByEmail(email)).thenReturn(new Member(id, email, name, password));

		mockMvc.perform(get("/oauth/member")
			.header("Authorization", "Bearer " + token)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andExpect(jsonPath("$.email").value(email))
			.andExpect(jsonPath("$.name").value(name))
			.andDo(LoginMemberDocumentation.getAuthorizedMember());
	}
}