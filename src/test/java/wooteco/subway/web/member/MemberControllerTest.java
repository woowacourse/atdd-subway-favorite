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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        MemberRequest memberRequest = new MemberRequest("ramen6315@gmail.com", "ramen", "6315)");

        given(memberService.createMember(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(DummyTestUserInfo.EMAIL);

        String content = gson.toJson(memberRequest);
        String uri = "/auth/members";

        mockMvc.perform(post(uri)
                .header("Location", "auth/members/"+member.getId())
                .header("Authorization", "Bearer " + "이메일 Token")
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

        String content = gson.toJson(memberRequest);
        String uri = "/auth/members";

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

        String uri = "/auth/members";

        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.get(uri)
                .param("email", email)
                .header("authorization", "Bearer 토큰값")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(MemberDocumentation.selectMember())
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

        String uri = "/auth/members?email=ramen@gmail.com";

        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("인증 정보 없는 수정하기 시 예외가 발생한다.")
    @Test
    void updateInfoTestFail() throws Exception {
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("coyle", "6315");

        given(memberService.updateMember(any(), any())).willReturn(1L);

        String updateData = gson.toJson(updateMemberRequest);
        String uri = "/auth/members";

        mockMvc.perform(put(uri)
                .content(updateData)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("수정하기 성공")
    @Test
    void updateInfoTestSuccess() throws Exception {
        Member member = new Member(1L, "ramen6315@gmail.com", "6315", "6315");

        given(memberService.updateMember(any(), any())).willReturn(1L);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(anyString())).willReturn("ramen6315@gmail.com");
        given(memberService.findMemberById(anyLong())).willReturn(member);

        Long updateId = member.getId();
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("coyle", "6315");
        String updateData = gson.toJson(updateMemberRequest);

        String uri = "/auth/members";

        mockMvc.perform(RestDocumentationRequestBuilders.put(uri, updateId)
                .header("Authorization", "Bearer 아무토큰값")
                .content(updateData)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MemberDocumentation.updateMember())
                .andDo(print())
                .andExpect(status().isOk());
    }
}
