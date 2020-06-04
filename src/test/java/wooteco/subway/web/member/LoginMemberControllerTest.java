package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_NAME;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_PASSWORD;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.util.List;
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
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.domain.favorite.FavoriteDetail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.member.exception.NotFoundMemberException;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginMemberControllerTest {
    @MockBean
    MemberService memberService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Member member;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
        this.member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    @DisplayName("로그인 요청")
    void login() throws Exception {
        given(memberService.createToken(any())).willReturn("token");

        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String inputJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/oauth/token")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token"))
                .andExpect(jsonPath("$.tokenType").value("bearer"))
                .andDo(print())
                .andDo(LoginMemberDocumentation.login());
    }

    @DisplayName("로그인 실패 - 유효하지 않은 형식")
    @ParameterizedTest
    @MethodSource("provideInvalidLoginRequest")
    void login_failure_invalid_input(String email, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(email, password);
        String inputJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/oauth/token")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Arguments> provideInvalidLoginRequest() {
        return Stream.of(
                Arguments.arguments("brownemail.com", "brown"),
                Arguments.arguments("brownemailcom", "brown"),
                Arguments.arguments("brown@email.com", "bro wn"),
                Arguments.arguments("brown@email.com", ""),
                Arguments.arguments("", "brown")
        );
    }

    @Test
    @DisplayName("내 정보 조회")
    void getMemberOfMine() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(get("/me")
                .header("Authorization", "Bearer access_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
                .andExpect(jsonPath("$.name").value(TEST_USER_NAME))
                .andDo(LoginMemberDocumentation.getMemberOfMine());
    }

    @Test
    @DisplayName("내 정보 조회 - 토큰이 유효하지 않는 경우")
    void getMemberOfMine_invalid_token() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(false);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(get("/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("내 정보 수정")
    void updateMemberOfMine() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("updateName",
                "updatePassword");
        String inputJson = objectMapper.writeValueAsString(updateMemberRequest);

        mockMvc.perform(
                patch("/me")
                        .header("Authorization", "Bearer access_token")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(LoginMemberDocumentation.updateMemberOfMine());
    }

    @Test
    @DisplayName("내 정보 수정 - 토큰이 유효하지 않는 경우")
    void updateMemberOfMine_invalid_token() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(false);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("updateName",
                "updatePassword");
        String inputJson = objectMapper.writeValueAsString(updateMemberRequest);

        mockMvc.perform(patch("/me")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("내 정보 수정 실패 - 유효하지 않은 형식")
    @ParameterizedTest
    @MethodSource("provideInvalidUpdateMemberRequest")
    void updateMemberOfMine_failure_invalid_input(String name, String password) throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest(name, password);
        String inputJson = objectMapper.writeValueAsString(updateMemberRequest);

        mockMvc.perform(patch("/me")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Arguments> provideInvalidUpdateMemberRequest() {
        return Stream.of(
                Arguments.arguments("", "brown"),
                Arguments.arguments(" ", "brown"),
                Arguments.arguments(" 브라운", "brown"),
                Arguments.arguments("브라운 ", "brown"),
                Arguments.arguments(" 브라운 ", "brown"),
                Arguments.arguments("브라운", "bro wn"),
                Arguments.arguments("브라운", " brown"),
                Arguments.arguments("브라운", "brown "),
                Arguments.arguments("브라운", " brown "),
                Arguments.arguments("브라운", " bro wn "),
                Arguments.arguments("브라운", " bro wn"),
                Arguments.arguments("브라운", "bro wn ")
        );
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteMemberOfMine() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(delete("/me")
                .header("Authorization", "Bearer access_token"))
                .andExpect(status().isNoContent())
                .andDo(LoginMemberDocumentation.deleteMemberOfMine());
    }

    @Test
    @DisplayName("회원 탈퇴 - 토큰이 유효하지 않는 경우")
    void deleteMemberOfMine_invalid_token() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(false);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(delete("/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("회원 탈퇴 - 토큰이 유효하지만 이미 회원 탈퇴한 경우")
    void deleteMemberOfMine_with_valid_token_after_delete_account() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.findMemberByEmail(any())).willThrow(NotFoundMemberException.class);

        mockMvc.perform(delete("/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void createFavorite() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        String inputJson = objectMapper.writeValueAsString(new FavoriteRequest(1L, 2L));

        mockMvc.perform(post("/me/favorites")
                .header("Authorization", "Bearer access_token")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.createFavorite());
    }

    @DisplayName("즐겨찾기 추가 실패 - 유효하지 않은 형식")
    @ParameterizedTest
    @MethodSource("provideInvalidFavoriteRequest")
    void createFavorite_failure_invalid_input(Long sourceId, Long targetId) throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        String inputJson = objectMapper.writeValueAsString(favoriteRequest);

        mockMvc.perform(post("/me/favorites")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Arguments> provideInvalidFavoriteRequest() {
        return Stream.of(
                Arguments.arguments(null, null),
                Arguments.arguments(null, 1L),
                Arguments.arguments(1L, null)
        );
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    void getFavorites() throws Exception {
        List<FavoriteDetail> favoriteDetails = Lists.newArrayList(
                new FavoriteDetail(1L, 2L, "잠실역", "삼성역"));
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.getFavorites(any())).willReturn(favoriteDetails);

        mockMvc.perform(get("/me/favorites")
                .header("Authorization", "Bearer access_token")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].sourceId").value(favoriteDetails.get(0).getSourceId()))
                .andExpect(jsonPath("[0].targetId").value(favoriteDetails.get(0).getTargetId()))
                .andExpect(jsonPath("[0].sourceName").value(favoriteDetails.get(0).getSourceName()))
                .andExpect(jsonPath("[0].targetName").value(favoriteDetails.get(0).getTargetName()))
                .andDo(print())
                .andDo(LoginMemberDocumentation.getFavorites());
    }

    @Test
    @DisplayName("즐겨찾기 여부 확인")
    void hasFavorite() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(memberService.hasFavorite(any(), anyLong(), anyLong())).willReturn(true);

        mockMvc.perform(get("/me/favorites/from/{sourceId}/to/{targetId}", 1L, 2L)
                .header("Authorization", "Bearer access_token")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist").value(true))
                .andDo(print())
                .andDo(LoginMemberDocumentation.hasFavorite());
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);

        mockMvc.perform(delete("/me/favorites/from/{sourceId}/to/{targetId}", 1L, 2L)
                .header("Authorization", "Bearer access_token"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LoginMemberDocumentation.deleteFavorite());
    }
}
