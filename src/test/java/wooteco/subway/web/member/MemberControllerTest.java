package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_NAME;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_PASSWORD;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void register_success() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);

        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME,
                TEST_USER_PASSWORD);
        String inputJson = objectMapper.writeValueAsString(memberRequest);

        mockMvc.perform(post("/members")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(MemberDocumentation.createMember());
    }

    @DisplayName("회원 가입 실패 - 유효하지 않은 형식")
    @ParameterizedTest
    @MethodSource("provideInvalidMemberRequest")
    void register_failure_invalid_input(String email, String name, String password) throws
            Exception {

        MemberRequest memberRequest = new MemberRequest(email, name, password);
        String inputJson = objectMapper.writeValueAsString(memberRequest);

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

    @Test
    @DisplayName("이메일로 회원 정보 조회")
    void getMemberByEmail() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(get("/members")
                .queryParam("email", TEST_USER_EMAIL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.getMemberByEmail());
    }

    @Test
    @DisplayName("회원 정보 수정")
    void updateMember() throws Exception {
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("updated_name",
                "updated_password");
        String inputJson = objectMapper.writeValueAsString(updateMemberRequest);

        mockMvc.perform(put("/members/{id}", 1L)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
    }

    @Test
    @DisplayName("회원 정보 삭제")
    void deleteMember() throws Exception {
        mockMvc.perform(delete("/members/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember());
    }
}
