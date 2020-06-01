package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.service.member.MemberServiceTest.*;

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

    @Test
    public void getMember() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(MEMBER_BROWN);
        given(jwtTokenProvider.isInvalidToken(any())).willReturn(false);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        mockMvc.perform(get("/members").header("Authorization",
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.readMember());
    }

    @Test
    public void updateMember() throws Exception {
        given(jwtTokenProvider.isInvalidToken(any())).willReturn(false);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        String inputJson = "{\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        mockMvc.perform(put("/members/{id}", 1L)
                .header("Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.updateMember());
    }

    @Test
    public void deleteMember() throws Exception {
        given(jwtTokenProvider.isInvalidToken(any())).willReturn(false);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        mockMvc.perform(delete("/members/{id}", 1L)
                .header("Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember());
    }
}
