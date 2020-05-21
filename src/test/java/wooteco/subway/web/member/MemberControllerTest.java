package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    // TODO: 회원가입 API 테스트

    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("회원가입 성공")
    void register_success() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @DisplayName("회원 가입 실패 - 유효하지 않은 형식")
    @ParameterizedTest
    @MethodSource("provideInvalidMemberRequest")
    void register_failure_invalid_input(String email, String name, String password) throws
        Exception {
        String inputJson = "{\"email\":\"" + email + "\"," +
            "\"name\":\"" + name + "\"," +
            "\"password\":\"" + password + "\"}";

        mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    private static Stream<Arguments> provideInvalidMemberRequest() {
        return Stream.of(
            Arguments.arguments("brownemail.com", "브라운", "brown"),
            Arguments.arguments("brown@email.com", "브 라운", "brown"),
            Arguments.arguments("brown@email.com", "브라운", "bro wn"),
            Arguments.arguments("brown@email.com", "", "brown"),
            Arguments.arguments("brown@email.com", "브라운", "")
        );
    }
}
