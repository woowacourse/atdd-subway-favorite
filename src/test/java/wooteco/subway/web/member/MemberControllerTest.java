package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.login.AuthorizationExtractor;
import wooteco.subway.web.member.login.JwtTokenProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = {MemberController.class, LoginMemberController.class})
public class MemberControllerTest {
    public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib29yd29uaWVAZW1haWwuY29tIiwiaWF0IjoxNTgxOTg1NjYzLCJleHAiOjE1ODE5ODkyNjN9.nL07LEhgTVzpUdQrOMbJq-oIce_idEdPS62hB2ou2hg";
    @MockBean
    protected MemberService memberService;
    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
    @MockBean
    protected AuthorizationExtractor authorizationExtractor;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void createMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
                "\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(MemberDocumentation.createMember());
    }

    @Test
    public void update() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(authorizationExtractor.extract(any(), anyString())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        String inputJson = "{\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(put("/me")
                .content(inputJson)
                .header("Authorization", "Bearer " + TEST_USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(MemberDocumentation.updateMember());
    }

    @Test
    public void updateWithoutLogin() throws Exception {
        given(authorizationExtractor.extract(any(), anyString())).willReturn(null);
        given(memberService.findMemberByEmail(any())).willThrow(RuntimeException.class);

        String inputJson = "{\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(put("/me")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
