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

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {
    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 요청")
    void login() throws Exception {
        given(memberService.createToken(any())).willReturn("token");

        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String inputJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/oauth/token").content(inputJson)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("token"))
            .andExpect(jsonPath("$.tokenType").value("bearer"))
            .andDo(print());
    }
}
