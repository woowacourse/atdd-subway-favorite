package woowa.bossdog.subway.web.member;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import woowa.bossdog.subway.domain.Member;
import woowa.bossdog.subway.service.Member.MemberService;
import woowa.bossdog.subway.service.Member.dto.MemberResponse;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("회원 생성")
    @Test
    void createMember() throws Exception {
        // given
        final MemberResponse memberResponse = MemberResponse.from(new Member(111L, "test@test.com", "bossdog", "test"));
        given(memberService.createMember(any())).willReturn(memberResponse);

        // when
        mvc.perform(post("/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"name\":\"bossdog\",\"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/members/" + memberResponse.getId()));

        // then
        verify(memberService).createMember(any());
    }

    @DisplayName("회원 목록 조회")
    @Test
    void listMember() throws Exception {
        // given
        List<MemberResponse> memberResponses = Lists.newArrayList(
                MemberResponse.from(new Member("test@test.com", "bossdog", "test")),
                MemberResponse.from(new Member("one@one.com", "onedog", "test")));
        given(memberService.listMembers()).willReturn(memberResponses);

        // when
        mvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));
        // then
        verify(memberService).listMembers();
    }

    @DisplayName("회원 단건 조회")
    @Test
    void findMember() throws Exception {
        // given
        MemberResponse response = MemberResponse.from(new Member(111L, "test@test.com", "bossdog", "test"));
        given(memberService.findMember(any())).willReturn(response);

        // when
        mvc.perform(get("/members/" + response.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        // then
        verify(memberService).findMember(eq(111L));
    }

    @DisplayName("회원 정보 수정")
    @Test
    void updateMember() throws Exception {
        // given
        final MemberResponse member = MemberResponse.from(new Member(111L, "test@test.com", "bossdog", "test"));
        given(memberService.findMember(any())).willReturn(member);

        // when
        mvc.perform(put("/members/" + member.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"changedName\",\"password\":\"changedPassword\"}"))
                .andExpect(status().isOk());

        // then
        verify(memberService).updateMember(eq(111L), any());
    }

    @DisplayName("회원 삭제")
    @Test
    void deleteMember() throws Exception {
        // given
        Member member = new Member(111L, "test@test.com", "bossdog", "test");

        // when
        mvc.perform(delete("/members/" + member.getId()))
                .andExpect(status().isNoContent());

        // then
        verify(memberService).deleteMember(eq(111L));
    }
}