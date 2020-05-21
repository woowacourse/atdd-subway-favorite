package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import wooteco.subway.doc.MemberLoginDocumentation;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;

/**
 *    class description
 *
 *    @author HyungJu An, MinWoo Yim
 */
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
class LoginMemberControllerTest {
	@MockBean
	protected MemberService memberService;

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.apply(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	void login() throws Exception {
		final String email = "a@email.com";
		final String password = "1234";
		LoginRequest loginRequest = new LoginRequest(email, password);
		String token = jwtTokenProvider.createToken(loginRequest.getEmail());

		given(memberService.createToken(any())).willReturn(token);

		String inputJson = "{\"email\":\"" + email + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberLoginDocumentation.createToken());
	}

	@DisplayName("유저 email이 빈 값이면 예외 처리한다.")
	@Test
	void login2() throws Exception {
		final String email = "";
		final String password = "1234";
		LoginRequest loginRequest = new LoginRequest(email, password);
		String token = jwtTokenProvider.createToken(loginRequest.getEmail());

		given(memberService.createToken(any())).willReturn(token);

		String inputJson = "{\"email\":\"" + email + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("유저 email 형식이 잘못되면 예외 처리한다.")
	@Test
	void login3() throws Exception {
		final String email = "asdfasdf";
		final String password = "1234";
		LoginRequest loginRequest = new LoginRequest(email, password);
		String token = jwtTokenProvider.createToken(loginRequest.getEmail());

		given(memberService.createToken(any())).willReturn(token);

		String inputJson = "{\"email\":\"" + email + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("유저 password가 빈 값이면 예외 처리한다.")
	@Test
	void login4() throws Exception {
		final String email = "a@email.com";
		final String password = "";
		LoginRequest loginRequest = new LoginRequest(email, password);
		String token = jwtTokenProvider.createToken(loginRequest.getEmail());

		given(memberService.createToken(any())).willReturn(token);

		String inputJson = "{\"email\":\"" + email + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
}