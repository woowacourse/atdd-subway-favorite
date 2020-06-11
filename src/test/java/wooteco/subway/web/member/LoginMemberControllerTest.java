package wooteco.subway.web.member;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.common.AuthorizationType;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.TokenResponse;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class LoginMemberControllerTest {

	private static final String TIGER_EMAIL = "tiger@luv.com";
	private static final String TIGER_PASSWORD = "prettiger";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	@DisplayName("토큰 로그인 - 회원 정보가 있는 경우(성공)")
	@Test
	void token_login_success() throws Exception {

		String token = "1234";
		TokenResponse jwtToken = new TokenResponse(token, AuthorizationType.BEARER.getPrefix());
		when(memberService.createJwtToken(any())).thenReturn(jwtToken);

		String body =
			"{\"email\" : \"" + TIGER_EMAIL + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";
		String expectedBody =
			"{\"accessToken\":\"" + token + "\",\"tokenType\":\"" + AuthorizationType.BEARER
				.getPrefix() + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(body)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedBody))
			.andDo(print());
	}

	@DisplayName("토큰 로그인 - 회원 정보가 없는 경우(실패)")
	@Test
	void token_login_fail() throws Exception {
		when(memberService.createJwtToken(any()))
			.thenThrow(new InvalidAuthenticationException("잘못된 패스워드"));

		String body =
			"{\"email\" : \"" + TIGER_EMAIL + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(body)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isFound())
			.andDo(print());
	}
}