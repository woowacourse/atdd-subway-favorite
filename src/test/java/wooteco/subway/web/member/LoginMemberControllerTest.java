package wooteco.subway.web.member;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.subway.exception.LoginFailException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.TokenResponse;

@Import({AuthorizationExtractor.class, JwtTokenProvider.class})
@WebMvcTest(LoginMemberController.class)
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
        when(memberService.createJwtToken(any())).thenThrow(new LoginFailException());

        String body = "{\"email\" : \"" + TIGER_EMAIL + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/oauth/token")
            .content(body)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(print());
    }
}