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

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(MemberDocumentation.createMember());
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
        TokenResponse token = login(member.getEmail(), member.getPassword());

        String inputJson = "{" + "\"name\":\"" + "NEW" + "\"," +
            "\"password\":\"" + "NEW" + "\"}";
        doNothing().when(memberService)
            .updateMember(member.getId(), new UpdateMemberRequest("NEW", "NEW"));
        this.mockMvc.perform(put("/members/" + member.getId())
            .header("Authorization", "Bearer " + token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson)
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.updateMember());
    }

    @Test
    void deleteUser() throws Exception {
        when(memberService.createMember(member)).thenCallRealMethod();
        TokenResponse token = login(member.getEmail(), member.getPassword());

        doNothing().when(memberService).deleteMember(member.getId());
        this.mockMvc.perform(delete("/members/" + member.getId())
            .header("Authorization", "Bearer" + token.getAccessToken())
        )
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(MemberDocumentation.deleteMember());
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
}
