package wooteco.subway.web.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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

    @Disabled
    @DisplayName("세션 로그인 - 회원 정보가 있는 경우(성공)")
    @Test
    void session_login_success() throws Exception {
        when(memberService.loginWithForm(any())).thenReturn(true);

        String body = "{\"email\" : \"" + TIGER_EMAIL + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";

        HttpSession session = this.mockMvc.perform(post("/login")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getRequest()
            .getSession();

        assertThat(session).isNotNull();
        assertThat(session.getAttribute("loginMemberEmail")).isEqualTo(TIGER_EMAIL);
    }

    @Disabled
    @DisplayName("세션 로그인 - 회원 정보가 없는 경우(실패)")
    @Test
    void session_login_fail() throws Exception {
        when(memberService.loginWithForm(any())).thenReturn(false);

        String body = "{\"email\" : \"" + TIGER_EMAIL + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";

        HttpSession session = this.mockMvc.perform(post("/login")
            .content(body)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(print())
            .andReturn()
            .getRequest()
            .getSession();

        assertThat(session).isNotNull();
        assertThat(session.getAttribute("loginMemberEmail")).isNull();
    }

	@DisplayName("토큰 로그인 - 회원 정보가 있는 경우(성공)")
	@Test
	void token_login_success() throws Exception {

        String token = "1234";
        TokenResponse jwtToken = new TokenResponse(token, "bearer");
        when(memberService.createJwtToken(any())).thenReturn(jwtToken);

        String body = "{\"email\" : \"" + TIGER_EMAIL + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";
        String expectedBody = "{\"accessToken\":\"" + token + "\",\"tokenType\":\"bearer\"}";

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
        when(memberService.createJwtToken(any())).thenThrow(new InvalidAuthenticationException("잘못된 패스워드"));

        String body = "{\"email\" : \"" + TIGER_EMAIL + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/oauth/token")
            .content(body)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isFound())
            .andDo(print());
    }

	@DisplayName("displayName")
	@Test
	void getMemberOfMineBasic() {
	}
}