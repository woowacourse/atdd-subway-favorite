package woowa.bossdog.subway.web.member;

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
import woowa.bossdog.subway.infra.JwtTokenProvider;
import woowa.bossdog.subway.service.Member.MemberService;
import woowa.bossdog.subway.service.Member.NotExistedEmailException;
import woowa.bossdog.subway.service.Member.WrongPasswordException;
import woowa.bossdog.subway.service.Member.dto.UpdateMemberRequest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginMemberControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private WebApplicationContext ctx;

    @MockBean private MemberService memberService;
    @MockBean private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        member = member = new Member(111L, "test@test.com", "bossdog", "test");
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(member.getEmail());
    }

    @DisplayName("로그인 실패 - 등록되지 않은 이메일")
    @Test
    void loginFailWithNotExistedEmail() throws Exception {
        // given
        given(memberService.createToken(any())).willThrow(NotExistedEmailException.class);

        // when
        mvc.perform(post("/me/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"notExsited@test.com\",\"password\":\"test\"}"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("로그인 실패 - 잘못된 패스워드")
    @Test
    void loginFailWithWrongPassword() throws Exception {
        // given
        given(memberService.createToken(any())).willThrow(WrongPasswordException.class);

        // when
        mvc.perform(post("/me/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"password\":\"wrong\"}"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        // given
        String token = "ACCESS_TOKEN";
        given(memberService.createToken(any())).willReturn(token);

        // when
        mvc.perform(post("/me/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"password\":\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"accessToken\":\"ACCESS_TOKEN\",\"tokenType\":\"Bearer\"}"));

        // then
        verify(memberService).createToken(any());
    }

    @DisplayName("로그인 정보 불러오기")
    @Test
    void getLoginMember() throws Exception {
        // given
        // when
        mvc.perform(get("/me")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "ACCESS_TOKEN"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":111,\"email\":\"test@test.com\","
                        + "\"name\":\"bossdog\",\"password\":\"test\"}"));

        // then
        verify(memberService).findMemberByEmail(eq(member.getEmail()));
    }

    @DisplayName("마이페이지 정보 수정")
    @Test
    void updateLoginMember() throws Exception {
        // given
        UpdateMemberRequest request = new UpdateMemberRequest("changedName", "changedPassword");

        // when
        mvc.perform(put("/me")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "ACCESS_TOKEN")
                .content("{\"name\":\"changedName\",\"password\":\"changedPassword\"}"))
                .andExpect(status().isOk());

        // then
        verify(memberService).updateMember(eq(111L), eq(request));
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteLoginMember() throws Exception {
        // given
        // when
        mvc.perform(delete("/me")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "ACCESS_TOKEN"))
                .andExpect(status().isNoContent());

        // then
        verify(memberService).deleteMember(eq(111L));
    }

}