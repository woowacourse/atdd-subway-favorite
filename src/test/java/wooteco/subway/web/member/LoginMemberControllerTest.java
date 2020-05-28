package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("로그인 요청을 보냈을때 OK응답이 오는지 테스트")
    @Test
    void loginTest() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.createToken(any())).willReturn(TEST_TOKEN);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/oauth/token")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.login());
    }

    @DisplayName("토큰으로 본인의 정보를 요청했을때 OK응답이 오는지 테스트")
    @Test
    void getOwnMemberTest() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(any())).willReturn(new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));

        this.mockMvc.perform(get("/me")
                .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.getOwnMember());
    }

    @DisplayName("토큰으로 본인의 정보를 수정했을때 OK응답이 오는지 테스트")
    @Test
    void updateOwnMemberTest() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        doNothing().when(memberService).updateMember(any(), any());

        String inputJson = "{\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(put("/me")
                .header("Authorization", "Bearer " + TEST_TOKEN)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.updateOwnMember());
    }

    @DisplayName("토큰으로 본인의 정보를 삭제했을때 NO_CONTENT응답이 오는지 테스트")
    @Test
    void deleteOwnMemberTest() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        doNothing().when(memberService).deleteMember(any());

        this.mockMvc.perform(delete("/me")
                .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LoginMemberDocumentation.deleteOwnMember());
    }
}
