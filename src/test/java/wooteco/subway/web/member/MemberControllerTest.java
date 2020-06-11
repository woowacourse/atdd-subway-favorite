package wooteco.subway.web.member;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.auth.AuthorizationExtractor;

@Import(HttpEncodingAutoConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(value = {MemberController.class, AuthorizationExtractor.class, JwtTokenProvider
    .class})
class MemberControllerTest {

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static final String EMAIL = "chomily@woowahan.com";
    static final String NAME = "chomily";
    static final String NEW_NAME = "쪼밀리";
    static final String PASSWORD = "chomily1234";
    static final Long ID = 1L;
    static final String TOKEN = "You are authorized!";
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("회원 가입 기능 테스트")
    @Test
    public void createMember() throws Exception {
        Member member = new Member(1L, EMAIL, NAME, PASSWORD);
        when(memberService.createMember(any())).thenReturn(member);

        String inputJson = "{\"email\":\"" + EMAIL + "\"," +
            "\"name\":\"" + NAME + "\"," +
            "\"password\":\"" + PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(MemberDocumentation.createMember());
    }

    @DisplayName("예외테스트: null 혹은 빈 값으로 회원 가입하는 경우")
    @Test
    void createMember_withNullOrBlankValues() throws Exception {
        //given
        HashMap<String, String> nullRequest = new HashMap<>();
        nullRequest.put("email", null);
        nullRequest.put("name", null);
        nullRequest.put("password", null);

        String nullJson = OBJECT_MAPPER.writeValueAsString(nullRequest);

        //when, then
        mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nullJson))
            .andDo(print())
            .andExpect(status().isBadRequest());

        //given
        HashMap<String, String> emptyRequest = new HashMap<>();
        emptyRequest.put("email", "");
        emptyRequest.put("name", "");
        emptyRequest.put("password", "");

        String emptyJson = OBJECT_MAPPER.writeValueAsString(emptyRequest);

        //when, then
        mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(emptyJson))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @DisplayName("예외테스트: 적절한 형식이 아닌 이메일로 회원가입하는 경우")
    @Test
    void createMember_withInvalidEmail() throws Exception {
        //given
        HashMap<String, String> invalidRequest = new HashMap<>();
        invalidRequest.put("email", "chomily");
        invalidRequest.put("name", NAME);
        invalidRequest.put("password", PASSWORD);

        String invalidJson = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        //when, then
        mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인 후 사용자 정보 조회 테스트")
    @Test
    void getMember() throws Exception {
        when(jwtTokenProvider.getSubject(anyString())).thenReturn(EMAIL);
        when(memberService.findMemberByEmail(EMAIL)).thenReturn(
            new Member(ID, EMAIL, NAME, PASSWORD));

        mockMvc.perform(get("/members")
            .header("Authorization", "Bearer " + TOKEN)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.email").value(EMAIL))
            .andExpect(jsonPath("$.name").value(NAME))
            .andDo(MemberDocumentation.getMember());
    }

    @DisplayName("로그인 후 사용자 정보 수정 테스트")
    @Test
    void updateMember() throws Exception {
        Member member = new Member(ID, EMAIL, NAME, PASSWORD);
        Member updatedMember = new Member(ID, EMAIL, NEW_NAME, PASSWORD);

        when(jwtTokenProvider.getSubject(anyString())).thenReturn(EMAIL);
        when(memberService.findMemberByEmail(EMAIL)).thenReturn(member);
        when(memberService.updateMemberByUser(any(Member.class),
            any(UpdateMemberRequest.class))).thenReturn(
            updatedMember);

        HashMap<String, String> updateMemberRequest = new HashMap<>();
        updateMemberRequest.put("name", NEW_NAME);
        updateMemberRequest.put("password", PASSWORD);
        String request = OBJECT_MAPPER.writeValueAsString(updateMemberRequest);

        mockMvc.perform(put("/members")
            .header("Authorization", "Bearer " + TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .accept(MediaType.APPLICATION_JSON)
            .content(request))
            .andDo(print())
            .andExpect(jsonPath("$.name").value(NEW_NAME))
            .andDo(MemberDocumentation.updateMember());
    }

    @DisplayName("예외테스트: 로그인 후 null 혹은 빈 값으로 사용자 정보를 수정하는 경우")
    @Test
    void updateMember_withNullOrBlankValues() throws Exception {
        //given
        HashMap<String, String> nullRequest = new HashMap<>();
        nullRequest.put("name", null);
        nullRequest.put("password", null);

        String nullJson = OBJECT_MAPPER.writeValueAsString(nullRequest);

        //when, then
        mockMvc.perform(put("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + TOKEN)
            .content(nullJson))
            .andDo(print())
            .andExpect(status().isBadRequest());

        //given
        HashMap<String, String> emptyRequest = new HashMap<>();
        emptyRequest.put("name", "");
        emptyRequest.put("password", "");

        String emptyJson = OBJECT_MAPPER.writeValueAsString(emptyRequest);

        //when, then
        mockMvc.perform(put("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + TOKEN)
            .content(emptyJson))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인 후 사용자 정보 삭제 테스트")
    @Test
    void deleteMember() throws Exception {
        when(jwtTokenProvider.getSubject(anyString())).thenReturn(EMAIL);
        when(memberService.findMemberByEmail(EMAIL)).thenReturn(
            new Member(ID, EMAIL, NAME, PASSWORD));
        doNothing().when(memberService).deleteMemberByUser(any(Member.class));

        mockMvc.perform(delete("/members")
            .header("Authorization", "Bearer" + TOKEN)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(MemberDocumentation.deleteMember());
    }
}
