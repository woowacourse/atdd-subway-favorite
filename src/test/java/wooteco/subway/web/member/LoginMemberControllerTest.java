package wooteco.subway.web.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {

    private static final String token = "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTkwMzk2NTM4LCJleHAiOjE1OTA0MDAxMzh9.hGKWE4UOqfoLQ5-MdhovWGhqNkOXuJFJTEDYRZHHsyk";

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();

        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(loginMemberMethodArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberMethodArgumentResolver.resolveArgument(any(), any(), any(),
            any())).willReturn(1L);
    }

    @Test
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        given(memberService.createToken(any(LoginRequest.class))).willReturn(token);

        this.mockMvc.perform(post("/oauth/token")
            .header("Authorization", token)
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(LoginMemberDocumentation.login());
    }

    @Test
    void getMemberOfMineBasic() throws Exception {
        MemberResponse memberResponse = new MemberResponse(1L, TEST_USER_EMAIL, TEST_USER_NAME);
        given(memberService.findMemberById(anyLong())).willReturn(memberResponse);

        String response = this.mockMvc.perform(get("/me")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(LoginMemberDocumentation.getMemberOfMineBasic())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(memberResponse));
    }

    @Test
    void updateMe() throws Exception {
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest(TEST_USER_EMAIL,
            TEST_USER_PASSWORD);

        this.mockMvc.perform(put("/me")
            .header("Authorization", token)
            .content(objectMapper.writeValueAsString(updateMemberRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(LoginMemberDocumentation.updateMe());

        verify(memberService).updateMember(anyLong(), any());

    }

    @Test
    void deleteMe() throws Exception {
        this.mockMvc.perform(delete("/me")
            .header("Authorization", token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(LoginMemberDocumentation.deleteMe());

        verify(memberService).deleteMember(anyLong());
    }
}
