package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.NoSuchMemberException;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberControllerTest extends AcceptanceTest {
    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;
    private Member member;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }

    @Test
    void createMember() throws Exception {
        when(memberService.createMember(any())).thenReturn(member);
        when(memberService.findMemberByEmail(any())).thenThrow(NoSuchMemberException.class);

        this.mockMvc.perform(post("/members")
            .content(createNewMemberJson(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(MemberDocumentation.createMember());
    }

    @ParameterizedTest
    @ValueSource(strings = {"asdasd", "qweqwe", "cvpxcv@asdasd@", "@", "asdasd   @ asdasd"})
    void invalidEmailException(String input) throws Exception {
        when(memberService.createMember(any())).thenReturn(member);

        badRequestMemberCreate(createNewMemberJson(input, TEST_USER_NAME, TEST_USER_PASSWORD));
    }

    @Test
    void duplicatedEmail() throws Exception {
        when(memberService.createMember(any())).thenThrow(DuplicateEmailException.class);

        badRequestMemberCreate(createNewMemberJson("orange@gmail.com", TEST_USER_NAME, TEST_USER_PASSWORD));
    }

    @ParameterizedTest
    @CsvSource(value = {"' ':hi:bye", "hi@hi:' ':bye", "hi@hi:bye:'  '"}, delimiter = ':')
    void emptyInput(String email, String name, String password) throws Exception {
        when(memberService.createMember(any())).thenReturn(member);

        badRequestMemberCreate(createNewMemberJson(email, name, password));
    }

    @Test
    void findByEmail() throws Exception {
        when(memberService.findMemberByEmail(member.getEmail())).thenReturn(member);

        this.mockMvc.perform(get("/members")
            .param("email", member.getEmail())
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.findByEmail());
    }

    @Test
    void updateUser() throws Exception {
        when(memberService.createMember(member)).thenCallRealMethod();
        doNothing().when(memberService)
            .updateMember(member.getId(), new UpdateMemberRequest("NEW", TEST_USER_PASSWORD, "NEW"));

        TokenResponse token = login(member.getEmail(), member.getPassword());
        String inputJson = "{" + "\"name\":\"" + "NEW" + "\"," +
            "\"password\":\"" + "NEW" + "\"}";

        this.mockMvc.perform(put("/members/" + member.getId())
            .header("Authorization", "Bearer " + token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson)
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.updateMember());
    }

    public TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return
            given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/oauth/token").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(TokenResponse.class);
    }

    private String createNewMemberJson(String email, String name, String password) {
        return "{\"email\":\"" + email + "\"," +
            "\"name\":\"" + name + "\"," +
            "\"password\":\"" + password + "\"}";
    }

    private void badRequestMemberCreate(String body) throws Exception {
        this.mockMvc.perform(post("/members")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is4xxClientError())
            .andDo(print());
    }
}
