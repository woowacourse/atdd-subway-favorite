package wooteco.subway.web.member;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.exceptions.GlobalExceptionHandler;
import wooteco.subway.web.exceptions.InvalidLoginException;
import wooteco.subway.web.exceptions.InvalidRegisterException;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = MemberController.class)
@Import({AuthorizationExtractor.class, JwtTokenProvider.class, GlobalExceptionHandler.class})
public class MemberControllerTest {

    static final String AUTHORIZATION = "authorization";
    static final String TEST_USER_EMAIL = "brown@email.com";
    static final String TEST_USER_NAME = "브라운";
    static final String TEST_USER_PASSWORD = "brown";
    static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicm93bkBlbWFpbC5jb20iLCJpYXQiOjE1OTAwNDEyNzEsImV4cCI6MTU5MDA0NDg3MX0.xqlqbBXFVhYvzGi9g37YncXAiupfSeWsaC8s0Km_LtQ";
    static final String TEST_USER_NAME2 = "터틀";
    static final String TEST_USER_PASSWORD2 = "turtle";
    static final String ENCODING = "UTF-8";

    @MockBean
    protected MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthorizationExtractor authorizationExtractor;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new CharacterEncodingFilter(ENCODING, true))
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .build();
    }

    @DisplayName("사용자 생성")
    @Test
    public void createMember() throws Exception {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(MemberResponse.of(member));

        String body = objectMapper.writeValueAsString(memberRequest);

        mockMvc.perform(post("/members")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isCreated())
            .andDo(MemberDocumentation.createMember());
    }

    @DisplayName("빈 필드를 입력한 사용자 생성")
    @Test
    public void createMemberWithEmptyFields() throws Exception {
        MemberRequest memberRequest = new MemberRequest("", "", "");
        given(memberService.createMember(any())).willThrow(InvalidRegisterException.class);

        String body = objectMapper.writeValueAsString(memberRequest);

        mockMvc.perform(post("/members")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isBadRequest())
            .andDo(MemberDocumentation.createMemberWithEmptyFields());
    }

    @DisplayName("사용자 로그인")
    @Test
    public void login() throws Exception {
        given(memberService.createToken(any())).willReturn(TEST_USER_TOKEN);
        String body = objectMapper.writeValueAsString(new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD));

        mockMvc.perform(post("/oauth/token")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk())
            .andDo(MemberDocumentation.login());
    }

    @DisplayName("사용자 로그인 실패")
    @Test
    public void invalidLogin() throws Exception {
        given(memberService.createToken(any())).willThrow(new InvalidLoginException("잘못된 패스워드"));
        String body = objectMapper.writeValueAsString(new LoginRequest(TEST_USER_EMAIL, "wrong password!!!"));

        mockMvc.perform(post("/oauth/token")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isBadRequest())
            .andDo(MemberDocumentation.invalidLogin());
    }

    @DisplayName("사용자 조회")
    @Test
    void getMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(authorizationExtractor.extract(any(), anyString())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(anyString())).willReturn(member);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/members")
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .param("email", TEST_USER_EMAIL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(MemberDocumentation.getMember());
    }

    @DisplayName("미인증 사용자 조회")
    @Test
    void getMemberWithoutAuthentication() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        // given(authorizationExtractor.extract(any(), anyString())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(anyString())).willReturn(member);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/members")
            .param("email", TEST_USER_EMAIL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(MemberDocumentation.getMemberWithoutAuth());
    }

    @DisplayName("사용자 수정")
    @Test
    void updateMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(authorizationExtractor.extract(any(), anyString())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(anyString())).willReturn(member);
        String body = objectMapper.writeValueAsString(new UpdateMemberRequest(TEST_USER_NAME2, TEST_USER_PASSWORD2));

        mockMvc.perform(RestDocumentationRequestBuilders.put("/members/{id}", member.getId())
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
            .andExpect(status().isOk())
            .andDo(MemberDocumentation.updateMember());
    }

    @DisplayName("사용자 삭제")
    @Test
    void deleteMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(authorizationExtractor.extract(any(), anyString())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(anyString())).willReturn(member);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/members/{id}", member.getId())
            .header(AUTHORIZATION, TEST_USER_TOKEN))
            .andExpect(status().isNoContent())
            .andDo(MemberDocumentation.deleteMember());
    }
}
