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
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.member.exception.NotFoundMemberException;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {
    @MockBean
    MemberService memberService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

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

        mockMvc.perform(post("/oauth/token")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("token"))
            .andExpect(jsonPath("$.tokenType").value("bearer"))
            .andDo(print());
    }

    @Test
    @DisplayName("내 정보 조회")
    void getMemberOfMine() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(get("/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
            .andExpect(jsonPath("$.name").value(TEST_USER_NAME));
    }

    @Test
    @DisplayName("내 정보 조회 - 토큰이 유효하지 않는 경우")
    void getMemberOfMine_invalid_token() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(false);

        mockMvc.perform(get("/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("내 정보 수정")
    void updateMemberOfMine() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("updateName",
            "updatePassword");
        String inputJson = objectMapper.writeValueAsString(updateMemberRequest);

        mockMvc.perform(
            patch("/me")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("내 정보 수정 - 토큰이 유효하지 않는 경우")
    void updateMemberOfMine_invalid_token() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(false);

        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("updateName",
            "updatePassword");
        String inputJson = objectMapper.writeValueAsString(updateMemberRequest);

        mockMvc.perform(patch("/me")
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteMemberOfMine() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(delete("/me"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("회원 탈퇴 - 토큰이 유효하지 않는 경우")
    void deleteMemberOfMine_invalid_token() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(false);

        mockMvc.perform(delete("/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("회원 탈퇴 - 토큰이 유효하지만 이미 회원 탈퇴한 경우")
    void deleteMemberOfMine_with_valid_token_after_delete_account() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willThrow(NotFoundMemberException.class);

        mockMvc.perform(delete("/me"))
            .andExpect(status().isUnauthorized());
    }
}
