package wooteco.subway.web.member;

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

    @Test
    public void login() throws Exception {
        given(memberService.createToken(any())).willReturn("this is accessToken");

        mvc.perform(post("/oauth/token")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"bossdog@email.com\",\"password\":\"dog\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"accessToken\":\"this is accessToken\",\"tokenType\":\"Bearer\"}"));

        verify(memberService).createToken(any());
    }

    @Test
    public void myInformation() throws Exception {
        final Member member = new Member(63L, "bossdog@email.com", "boss", "dog");
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(member.getEmail());

        mvc.perform(get("/me/bearer")
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