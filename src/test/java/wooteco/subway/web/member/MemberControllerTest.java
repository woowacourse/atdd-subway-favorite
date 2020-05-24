package wooteco.subway.web.member;

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
import wooteco.subway.domain.member.Member;
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
    // TODO: 회원가입 API 테스트

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

    @Test
    public void create() throws Exception {
        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);

        mvc.perform(post("/members")
                .content("{\"email\":\"bossdog@email.com\", \"name\":\"boss\", \"password\":\"dog\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/" + member.getId()));

        verify(memberService).createMember(any());
    }

    @Test
    public void show() throws Exception {
        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mvc.perform(get("/members?email=" + TEST_USER_EMAIL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("{\"id\":63,\"email\":\"bossdog@email.com\",\"name\":\"boss\"}"));

        verify(memberService).findMemberByEmail(eq(TEST_USER_EMAIL));
    }

    @Test
    public void update() throws Exception {
        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        mvc.perform(put("/members/" + member.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"보스독\",\"password\":\"ysys\"}"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(memberService).updateMember(eq(member.getId()), any());
    }

    @Test
    public void deleteMember() throws Exception {
        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        mvc.perform(delete("/members/" + member.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(eq(member.getId()));
    }
}
