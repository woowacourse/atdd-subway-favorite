package wooteco.subway;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import wooteco.subway.config.ETagHeaderFilter;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.LoginMemberMethodArgumentResolver;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

@Import(ETagHeaderFilter.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private BearerAuthInterceptor bearerAuthInterceptor;
    @MockBean
    private LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

    @BeforeEach
    void setUp() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(loginMemberMethodArgumentResolver.resolveArgument(any(), any(), any(),
            any())).willReturn(member);
        given(loginMemberMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    }

    @DisplayName("로그인을 시도하여 토큰을 얻는다.")
    @Test
    void tokenLogin() throws Exception {
        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        given(memberService.createToken(any())).willReturn("brown");

        final MvcResult mvcResult = this.mockMvc.perform(post("/oauth/token")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains("brown");
    }

    @DisplayName("이메일을 통해 MemberResponse를 받는다.")
    @Test
    void meBearer() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(get("/me/bearer")
            .sessionAttr("loginMemberEmail", TEST_USER_EMAIL)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).contains(TEST_USER_EMAIL);
    }

    @DisplayName("회원 정보 수정")
    @Test
    void edit() throws Exception {
        String inputJson = "{\"name\":\"" + "CU" + "\"," +
            "\"password\":\"" + "1234" + "\"}";

        final MvcResult mvcResult = this.mockMvc.perform(put("/me")
            .sessionAttr("loginMemberEmail", TEST_USER_EMAIL)
            .accept(MediaType.APPLICATION_JSON)
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @DisplayName("회원 정보 삭제")
    @Test
    void deleteMember() throws Exception {
        this.mockMvc.perform(delete("/me")
            .sessionAttr("loginMemberEmail", TEST_USER_EMAIL)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }
}
