package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.TEST_BEARER_TOKEN;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_NAME;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

@Import(value = {BearerAuthInterceptor.class, AuthorizationExtractor.class})
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest {
    private static final Member MEMBER_BROWN = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

    protected MockMvc mockMvc;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @DisplayName("사용자 추가")
    @Test
    public void createMemberTest() throws Exception {
        given(memberService.createMember(any())).willReturn(MEMBER_BROWN);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
                "\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        mockMvc.perform(post("/members/signup")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(MemberDocumentation.createMember());
    }

    @DisplayName("사용자 조회")
    @Test
    public void getMember() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(MEMBER_BROWN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        mockMvc.perform(get("/members").header(AuthorizationExtractor.AUTHORIZATION, TEST_BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.readMember());
    }

    @DisplayName("사용자 업데이트")
    @Test
    public void updateMember() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        String inputJson = "{\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        mockMvc.perform(put("/members")
                .header(AuthorizationExtractor.AUTHORIZATION, TEST_BEARER_TOKEN)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
    }

    @DisplayName("사용자 제거")
    @Test
    public void deleteMember() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        mockMvc.perform(delete("/members")
                .header(AuthorizationExtractor.AUTHORIZATION, TEST_BEARER_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember());
    }
}
