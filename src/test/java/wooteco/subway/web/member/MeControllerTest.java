package wooteco.subway.web.member;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.doc.MeDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MeControllerTest {
    private static final long TIGER_ID = 1L;
    private static final String TIGER_EMAIL = "tiger@luv.com";
    private static final String TIGER_NAME = "tiger";
    private static final String TIGER_PASSWORD = "prettiger";

    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        token = "bearer " + jwtTokenProvider.createToken(TIGER_EMAIL);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("토큰을 이용해서 내 정보를 조회한다. OK 상태코드를 반환하고, 해당 회원의 정보를 반환하는지 확인한다.")
    @Test
    void getMemberByEmail() throws Exception {
        Member member = new Member(TIGER_ID, TIGER_EMAIL, TIGER_NAME, TIGER_PASSWORD);
        given(memberService.findMemberByEmail(TIGER_EMAIL)).willReturn(member);

        String expected =
            "{\"email\" : \"" + TIGER_EMAIL + "\", \"name\" : \"" + TIGER_NAME + "\", \"id\" : " + TIGER_ID + "}";

        this.mockMvc.perform(get("/me")
            .header(HttpHeaders.AUTHORIZATION, token)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andDo(print())
            .andDo(MeDocumentation.getMember())
        ;
    }

    @DisplayName("토큰을 이용해서 내 정보를 조회한다. 토큰이 없거나 유효하지 않는 경우 302 코드 반환, Location 헤더에 로그인 페이지 표시.")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"Bearer 1234", "Bearer 1234.1234.1234"})
    void getMemberByEmail_notExistEmail(String token) throws Exception {

        this.mockMvc.perform(get("/me")
            .header(HttpHeaders.AUTHORIZATION, token)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isFound())
            .andExpect(header().string(HttpHeaders.LOCATION, "/login"))
            .andDo(print())
            .andDo(MeDocumentation.getMemberException())
        ;
    }

    @DisplayName("토큰값에 해당하는 계정의 회원 정보를 수정한다. 정보 수정후 OK 코드를 반환한다.")
    @Test
    void updateMember() throws Exception {
        Member member = new Member(TIGER_ID, TIGER_EMAIL, TIGER_NAME, TIGER_PASSWORD);
        given(memberService.findMemberByEmail(TIGER_EMAIL)).willReturn(member);
        String body = "{\"name\" : \"" + TIGER_NAME + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/me")
            .content(body)
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MeDocumentation.updateMember())
        ;
    }

    @DisplayName("토큰이 없거나 유효하지 않는 경우 정보 수정 시, 302 코드 반환, Location 헤더에 로그인 페이지 표시.")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"Bearer 1234", "Bearer 1234.1234.1234"})
    void updateMember_invalidToken(String token) throws Exception {
        String body = "{\"name\" : \"" + TIGER_NAME + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/me")
            .content(body)
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isFound())
            .andExpect(header().string(HttpHeaders.LOCATION, "/login"))
            .andDo(print())
            .andDo(MeDocumentation.updateMemberException())
        ;
    }

    @DisplayName("토큰에 포함된 이메일 회원 정보를 삭제한 후, NoContent 상태코드를 반환한다.")
    @Test
    void deleteMember() throws Exception {
        Member member = new Member(TIGER_ID, TIGER_EMAIL, TIGER_NAME, TIGER_PASSWORD);
        given(memberService.findMemberByEmail(TIGER_EMAIL)).willReturn(member);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/me")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(MeDocumentation.deleteMember())
        ;
    }

    @DisplayName("토큰이 없거나 무효한 경우, 내 정보 삭제시, 302 코드 반환, Location 헤더에 로그인 페이지 표시.")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"Bearer 1234", "Bearer 1234.1234.1234"})
    void deleteMember_invalidToken(String token) throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/me")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, token))
            .andExpect(status().isFound())
            .andExpect(header().string(HttpHeaders.LOCATION, "/login"))
            .andDo(print())
            .andDo(MeDocumentation.deleteMemberException())
        ;
    }
}
