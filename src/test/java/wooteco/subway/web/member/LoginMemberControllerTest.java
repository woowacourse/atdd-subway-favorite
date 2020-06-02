package wooteco.subway.web.member;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginMemberControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("올바른 이메일과 비밀번호가 입력되면 로그인이 성공한다")
    @Test
    void successLoginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest(DummyTestUserInfo.EMAIL,DummyTestUserInfo.PASSWORD);
        given(memberService.createToken(loginRequest)).willReturn("testToken");

        String uri = "/login";

        String content = OBJECT_MAPPER.writeValueAsString(loginRequest);

        MvcResult mvcResult = mockMvc.perform(post(uri)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.login())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).contains("testToken");
    }

    @DisplayName("이메일 형식이 올바르지 않을경우 BAD_REQUEST를 반환한다.")
    @Test
    void failLoginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("DummyTestUserInfo.EMAIL",DummyTestUserInfo.PASSWORD);
        given(memberService.createToken(loginRequest)).willReturn("testToken");

        String uri = "/login";

        String content = OBJECT_MAPPER.writeValueAsString(loginRequest);

        mockMvc.perform(post(uri)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("비밀 번호 형식이 올바르지 않을경우 BAD_REQUEST를 반환한다.")
    @Test
    void failLoginTest2() throws Exception {
        LoginRequest loginRequest = new LoginRequest(DummyTestUserInfo.EMAIL,"123");
        given(memberService.createToken(loginRequest)).willReturn("testToken");

        String uri = "/login";

        String content = OBJECT_MAPPER.writeValueAsString(loginRequest);

        mockMvc.perform(post(uri)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}