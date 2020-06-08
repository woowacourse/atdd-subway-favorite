package wooteco.subway.web.member;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.ExistedEmailException;
import wooteco.subway.service.member.MemberService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("회원 가입")
    @Test
    public void create() throws Exception {
        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);

        mvc.perform(post("/members")
                .content("{\"email\":\"bossdog@email.com\", \"name\":\"boss\", \"password\":\"dog\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/" + member.getId()))
                .andDo(print())
                .andDo(MemberDocumentation.createMember());

        verify(memberService).createMember(any());
    }

    @DisplayName("이미 존재하는 메일로 회원가입 요청")
    @Test
    public void createWithExistedEmail() throws Exception {
        given(memberService.createMember(any())).willThrow(ExistedEmailException.class);
        mvc.perform(post("/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"existed@email.com\", \"name\":\"existed\", \"password\":\"existed\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("이메일로 회원 조회")
    @Test
    public void show() throws Exception {
        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mvc.perform(get("/members?email=" + TEST_USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("{\"id\":63,\"email\":\"bossdog@email.com\",\"name\":\"boss\"}"))
                .andDo(print())
                .andDo(MemberDocumentation.getMember());

        verify(memberService).findMemberByEmail(eq(TEST_USER_EMAIL));
    }

    @DisplayName("회원 정보 수정")
    @Test
    public void update() throws Exception {
        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        mvc.perform(put("/members/" + member.getId())
                .header("Authorization", "mock token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"보스독\",\"password\":\"ysys\"}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
        verify(memberService).updateMember(eq(member.getId()), any());
    }

    @DisplayName("회원 삭제(탈퇴)")
    @Test
    public void deleteMember() throws Exception {
        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        mvc.perform(delete("/members/" + member.getId()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember());

        verify(memberService).deleteMember(eq(member.getId()));
    }
}
