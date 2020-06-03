package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.AcceptanceTest.*;

import javax.servlet.http.Cookie;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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

import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.exception.InvalidTokenException;
import wooteco.subway.web.member.exception.NotFoundMemberException;
import wooteco.subway.web.member.exception.NotMatchPasswordException;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    private Member member;
    private Cookie cookie;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();

        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        cookie = new Cookie("token", "dundung");
    }

    @Test
    public void createMember() throws Exception {
        given(memberService.save(any())).willReturn(member);
        given(memberService.isNotExistEmail(any())).willReturn(true);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"," +
            "\"passwordCheck\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(MemberDocumentation.createMember());
    }

    @Test
    void createDuplicateMember() throws Exception {
        given(memberService.save(any())).willReturn(member);
        given(memberService.isNotExistEmail(any())).willReturn(true);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"," +
            "\"passwordCheck\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(MemberDocumentation.createMember());

        given(memberService.isNotExistEmail(any())).willReturn(false);

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(MemberDocumentation.createDuplicateMember());
    }

    @Test
    void createNotMatchPasswordMember() throws Exception {
        given(memberService.save(any())).willReturn(member);
        given(memberService.isNotExistEmail(any())).willReturn(true);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"," +
            "\"passwordCheck\":\"" + TEST_OTHER_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(MemberDocumentation.createNotMatchPasswordMember());
    }

    @Test
    void login() throws Exception {
        given(memberService.createToken(any())).willReturn("token");

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken", Matchers.is("token")))
            .andExpect(jsonPath("$.tokenType", Matchers.is("bearer")))
            .andDo(print())
            .andDo(MemberDocumentation.login());
    }

    @Test
    void loginNotExistEmail() throws Exception {
        given(memberService.createToken(any())).willThrow(new NotFoundMemberException(TEST_OTHER_USER_EMAIL));

        String inputJson = "{\"email\":\"" + TEST_OTHER_USER_EMAIL + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(MemberDocumentation.loginNotExistEmail());
    }

    @Test
    void loginNotMatchPassword() throws Exception {
        given(memberService.createToken(any())).willThrow(new NotMatchPasswordException("비밀번호가 일치하지 않습니다."));

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"password\":\"" + TEST_OTHER_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(MemberDocumentation.loginNotMatchPassword());
    }

    @Test
    void getMember() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);

        this.mockMvc.perform(get("/members")
            .cookie(cookie)
            .param("email", TEST_USER_EMAIL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email", Matchers.is(TEST_USER_EMAIL)))
            .andExpect(jsonPath("$.name", Matchers.is(TEST_USER_NAME)))
            .andDo(print())
            .andDo(MemberDocumentation.getMember());
    }

    @Test
    void getNotExistMember() throws Exception {
        given(memberService.findMemberByEmail(any())).willThrow(new NotFoundMemberException("이메일을 찾을 수 없습니다."));
        given(jwtTokenProvider.validateToken(any())).willReturn(true);

        this.mockMvc.perform(get("/members")
            .cookie(cookie)
            .param("email", TEST_USER_EMAIL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(MemberDocumentation.getNotExistMember());
    }

    @Test
    void updateMember() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        String inputJson = "{\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"oldPassword\":\"" + TEST_USER_PASSWORD + "\"," +
            "\"newPassword\":\"" + "NEW_" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(put("/members/" + 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .cookie(cookie)
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.updateMember());
    }

    @Test
    void notExistTokenUpdateMember() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willThrow(new InvalidTokenException("토큰이 존재하지 않습니다."));
        String inputJson = "{\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"oldPassword\":\"" + TEST_USER_PASSWORD + "\"," +
            "\"newPassword\":\"" + "NEW_" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(put("/members/" + 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(MemberDocumentation.notExistTokenUpdateMember());
    }

    @Test
    void deleteMember() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        this.mockMvc.perform(delete("/members/" + 1L)
            .cookie(cookie))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(MemberDocumentation.deleteMember());
    }
}