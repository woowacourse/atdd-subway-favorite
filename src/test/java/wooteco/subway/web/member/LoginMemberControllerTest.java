package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.subway.service.member.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {
    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("로그인 요청")
    void login() throws Exception {
        given(memberService.createToken(any())).willReturn("token");

        String inputJson =
            "{\"email\":\"" + TEST_USER_EMAIL + "\"," + "\"password\":\"" + TEST_USER_PASSWORD
                + "\"}";

        mockMvc.perform(post("/oauth/token").content(inputJson)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"accessToken\":\"token\",\"tokenType\":\"bearer\"}"))
            .andDo(print());
    }
}
