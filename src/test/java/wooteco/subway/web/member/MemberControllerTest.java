package wooteco.subway.web.member;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    private static final Gson gson = new Gson();

    @MockBean
    MemberService memberService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("올바른 값이 입력되면 회원가입이 된다")
    @Test
    void createMemberTest() throws Exception {
        Member member = new Member(1L, "ramen6315@gmail.com", "ramen", "6315)");
        given(memberService.createMember(any())).willReturn(member);

        MemberRequest memberRequest = new MemberRequest("ramen6315@gmail.com", "ramen", "6315)");

        String uri = "/members";
        String content = gson.toJson(memberRequest);

        mockMvc.perform(post(uri)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MemberDocumentation.createMember())
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("올바르지 않은 값이 입력되면 회원가입이 안된다 - email 형식 X")
    @Test
    void createMemberTestFail1() throws Exception {
        Member member = new Member(1L, "ramen6315@gmail.com", "ramen", "6315)");
        MemberRequest memberRequest = new MemberRequest("이메일형식X", "ramen", "6315)");
        given(memberService.createMember(any())).willReturn(member);

        String uri = "/members";
        String content = gson.toJson(memberRequest);

        mockMvc.perform(post(uri)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("올바르지 않은 값이 입력되면 회원가입이 안된다 - 이름 빈값")
    @Test
    void createMemberTestFail2() throws Exception {
        Member member = new Member(1L, "ramen6315@gmail.com", "ramen", "6315)");
        MemberRequest memberRequest = new MemberRequest("이메일형식X", "", "6315)");
        given(memberService.createMember(any())).willReturn(member);

        String uri = "/members";
        String content = gson.toJson(memberRequest);

        mockMvc.perform(post(uri)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("올바르지 않은 값이 입력되면 회원가입이 안된다 - 비밀번호 3자 이하")
    @Test
    void createMemberTestFail3() throws Exception {
        Member member = new Member(1L, "ramen6315@gmail.com", "ramen", "631");
        MemberRequest memberRequest = new MemberRequest("이메일형식X", "ramen", "631");
        given(memberService.createMember(any())).willReturn(member);

        String uri = "/members";
        String content = gson.toJson(memberRequest);

        mockMvc.perform(post(uri)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("올바르지 않은 값이 입력되면 회원가입이 안된다 - null")
    @Test
    void createMemberTestFail4() throws Exception {
        Member member = new Member(1L, "ramen6315@gmail.com", "ramen", "631");
        MemberRequest memberRequest = new MemberRequest(null, null, null);
        given(memberService.createMember(any())).willReturn(member);

        String uri = "/members";
        String content = gson.toJson(memberRequest);

        mockMvc.perform(post(uri)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("정보조회 테스트")
    @Test
    void getMemberByEmailTest() throws Exception {
        String email = "ramen@gmail.com";
        Member member = new Member(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        given(memberService.findMemberByEmail(email)).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(email);
        String uri = "/members?email=ramen@gmail.com";

        MvcResult mvcResult = mockMvc.perform(get(uri)
                .header("authorization","Bearer 토큰값")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MemberResponse memberResponse = gson.fromJson(mvcResult.getResponse().getContentAsString(), MemberResponse.class);

        assertThat(memberResponse.getName()).isEqualTo("ramen");
        assertThat(memberResponse.getEmail()).isEqualTo("ramen@gmail.com");
    }

    @DisplayName("정보 조회 실패 테스트 (JWT Token이 없을 경우)")
    @Test
    void getMemberByEmailFailTest() throws Exception {
        String email = "ramen@gmail.com";
        Member member = new Member(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        given(memberService.findMemberByEmail(email)).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);

        String uri = "/members?email=ramen@gmail.com";

        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
