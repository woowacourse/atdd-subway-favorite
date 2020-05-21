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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.auth.Authentication;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginMemberControllerTest {

    private static final String EMAIL = "pci2676@gmail.com";
    private static final String NAME = "박찬인";
    private static final String PASSWORD = "1234";

    @MockBean
    private MemberService memberService;

    @MockBean
    private Authentication authentication;
    @MockBean
    private BearerAuthInterceptor bearerAuthInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);
        String loginRequestContent = objectMapper.writeValueAsString(loginRequest);

        when(memberService.createToken(any(LoginRequest.class))).thenReturn("asdf.zxcv.qewr");

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(loginRequestContent)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.loginMember());
    }

    @Test
    void getMyInfo() throws Exception {
        String token = "bearer asdf.zxcv.qewr";

        when(bearerAuthInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(authentication.getAuthentication(any(), any())).thenReturn(EMAIL);
        when(memberService.findMemberByEmail(any())).thenReturn(new Member(1L, EMAIL, NAME, PASSWORD));

        mockMvc.perform(get("/me")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.getMyInfo());
    }

    @Test
    void deleteMyInfo() throws Exception {
        String token = "bearer asdf.zxcv.qewr";

        when(bearerAuthInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(authentication.getAuthentication(any(), any())).thenReturn(EMAIL);

        doNothing().when(memberService).deleteByEmail(any());

        mockMvc.perform(delete("/me")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LoginMemberDocumentation.deleteMyInfo());
    }

    @Test
    void updateMyInfo() throws Exception {
        String token = "bearer asdf.zxcv.qewr";
        UpdateMemberRequest request = new UpdateMemberRequest(NAME, PASSWORD);
        String updateRequestContent = objectMapper.writeValueAsString(request);

        when(bearerAuthInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(authentication.getAuthentication(any(), any())).thenReturn(EMAIL);
        doNothing().when(memberService).updateMember(any(), any());

        mockMvc.perform(put("/me")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(updateRequestContent)
        )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LoginMemberDocumentation.updataMyInfo());
    }
}