package wooteco.subway.web.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

@WebMvcTest(controllers = LoginMemberController.class)
class LoginMemberControllerTest {

    private static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicm93bkBlbWFpbC5jb20iLCJpYXQiOjE1OTAwNDEyNzEsImV4cCI6MTU5MDA0NDg3MX0.xqlqbBXFVhYvzGi9g37YncXAiupfSeWsaC8s0Km_LtQ";
    private static final String TEST_USER_EMAIL = "brown@email.com";
    private static final String TEST_USER_NAME = "브라운";
    private static final String TEST_USER_PASSWORD = "brown";
    private static final String ENCODING = "UTF-8";
    private static final String AUTHORIZATION = "authorization";

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthorizationExtractor authorizationExtractor;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter(ENCODING, true))
                .addFilter(new ShallowEtagHeaderFilter())
                .alwaysDo(print())
                .build();
    }

    @DisplayName("토큰 발급 테스트")
    @Test
    void login() throws Exception {
        LoginRequest request = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String body = objectMapper.writeValueAsString(request);
        given(memberService.createToken(any())).willReturn(TEST_USER_TOKEN);

        String result = mockMvc.perform(post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TokenResponse tokenResponse = objectMapper.readValue(result, TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isEqualTo(TEST_USER_TOKEN);
    }

    @DisplayName("토큰을 통해 멤버 정보를 가져오는 테스트")
    @Test
    void getMemberOfMineBasic() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(authorizationExtractor.extract(any(), anyString())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(anyString())).willReturn(member);


        String result = mockMvc.perform(get("/me/bearer")
                .header(AUTHORIZATION, TEST_USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MemberResponse memberResponse = objectMapper.readValue(result, MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
    }
}