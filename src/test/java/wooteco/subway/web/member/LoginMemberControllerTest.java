package wooteco.subway.web.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.config.ETagHeaderFilter;
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

@Import(ETagHeaderFilter.class)
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @MockBean
    private BearerAuthInterceptor bearerAuthInterceptor;
    @MockBean
    private LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        Member member = new Member(1L,TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(loginMemberMethodArgumentResolver.resolveArgument(any(), any(), any(),
            any())).willReturn(member);
        given(loginMemberMethodArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberService.createToken(any())).willReturn("brownToken");

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("회원가입을 한다")
    @Test
    void createMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/me")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(LoginMemberDocumentation.createMember());
    }

    @DisplayName("로그인을 시도하여 토큰을 얻는다.")
    @Test
    void tokenLogin() throws Exception {
        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/me/login")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(LoginMemberDocumentation.login());
    }

    @DisplayName("회원 정보 조회")
    @Test
    void meBearer() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/me")
            .header("Authorization", "bearer brownToken")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andDo(LoginMemberDocumentation.getMember());
    }

    @DisplayName("회원 정보 수정")
    @Test
    void edit() throws Exception {
        String inputJson = "{\"name\":\"" + "CU" + "\"," +
            "\"password\":\"" + "1234" + "\"}";

        this.mockMvc.perform(put("/me")
            .header("Authorization", "bearer brownToken")
            .accept(MediaType.APPLICATION_JSON)
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(LoginMemberDocumentation.editMember());
    }

    @DisplayName("회원 정보 삭제")
    @Test
    void deleteMember() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/me")
            .header("Authorization", "bearer brownToken")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(LoginMemberDocumentation.deleteMember());
    }

    @DisplayName("즐겨찾기에 경로를 추가한다")
    @Test
    void addFavorites() throws Exception {
        String inputJson = "{\"sourceStationId\":\"" + "1" + "\"," +
            "\"targetStationId\":\"" + "2" + "\"}";

        this.mockMvc.perform(post("/me/favorites")
            .header("Authorization", "bearer brownToken")
            .accept(MediaType.APPLICATION_JSON)
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @DisplayName("즐겨찾기에 있는 경로를 삭제한다")
    @Test
    void deleteFavorites() throws Exception {
        String inputJson = "{\"sourceStationId\":\"" + "1" + "\"," +
            "\"targetStationId\":\"" + "2" + "\"}";

        this.mockMvc.perform(post("/me/favorites")
            .header("Authorization", "bearer brownToken")
            .accept(MediaType.APPLICATION_JSON)
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());

        String deleteInputJson = "{\"sourceStationId\":\"" + "1" + "\"," +
            "\"targetStationId\":\"" + "2" + "\"}";

        this.mockMvc.perform(delete("/me/favorites")
        .header("Authorization", "bearer brownToken")
            .accept(MediaType.APPLICATION_JSON)
            .content(deleteInputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());
    }




}
