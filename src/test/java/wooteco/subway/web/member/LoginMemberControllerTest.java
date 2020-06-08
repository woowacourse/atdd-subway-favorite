package wooteco.subway.web.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.NotExistedEmailException;
import wooteco.subway.service.member.WrongPasswordException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginMemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("로그인")
    @Test
    public void login() throws Exception {
        given(memberService.createToken(any())).willReturn("this is accessToken");

        mvc.perform(post("/me/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"bossdog@email.com\",\"password\":\"dog\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"accessToken\":\"this is accessToken\",\"tokenType\":\"Bearer\"}"));

        verify(memberService).createToken(any());
    }

    @DisplayName("등록되지 않은 이메일로 로그인")
    @Test
    public void loginWithNotExistedEmail() throws Exception {
        given(memberService.createToken(any())).willThrow(NotExistedEmailException.class);

        mvc.perform(post("/me/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"notExisted@email.com\",\"password\":\"dog\"}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("잘못된 패스워드로 로그인")
    @Test
    public void loginWithWrongPassword() throws Exception {
        given(memberService.createToken(any())).willThrow(WrongPasswordException.class);

        mvc.perform(post("/me/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"bossdog@email.com\",\"password\":\"wrong password\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("내 정보 불러오기")
    @Test
    public void myInformation() throws Exception {
        final Member member = new Member(63L, "bossdog@email.com", "boss", "dog");
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(member.getEmail());

        mvc.perform(get("/me")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer mockToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":63,\"email\":\"bossdog@email.com\","
                        + "\"name\":\"boss\"}"));

        verify(memberService).findMemberByEmail(eq(member.getEmail()));
    }
}