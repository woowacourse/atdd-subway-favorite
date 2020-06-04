package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.service.member.MemberServiceTest.*;

@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @MockBean
    protected MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

    @Value("${security.jwt.token.secret-key}")
    protected String secretKey;

    @Value("${security.jwt.token.expire-length}")
    protected Long validityInMilliseconds;

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

        this.mockMvc.perform(post("/members/new")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(MemberDocumentation.createMember());
    }

    @Test
    void updateMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        LoginRequest request = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(memberService.createToken(any())).willReturn(new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL));

        String inputJson = "{\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(put("/members")
                .header("Authorization", "bearer " + memberService.createToken(request))
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
    }

    @Test
    void findMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        LoginRequest request = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(memberService.createToken(any())).willReturn(new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL));


        this.mockMvc.perform(get("/members")
                .param("email", TEST_USER_EMAIL)
                .header("Authorization", "bearer " + memberService.createToken(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.findMember());
    }

    @Test
    void removeMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        LoginRequest request = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(memberService.createToken(any())).willReturn(new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL));


        this.mockMvc.perform(delete("/members")
                .header("Authorization", "bearer " + memberService.createToken(request)))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.removeMember());
    }
}