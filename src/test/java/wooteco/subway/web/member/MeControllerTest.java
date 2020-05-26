package wooteco.subway.web.member;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.doc.MeDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteExistenceResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.station.dto.StationResponse;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class MeControllerTest {
    private static final long TIGER_ID = 1L;
    private static final String TIGER_EMAIL = "tiger@luv.com";
    private static final String TIGER_NAME = "tiger";
    private static final String TIGER_PASSWORD = "prettiger";

    private static final Long SOURCE_STATION_ID = 1L;
    private static final Long TARGET_STATION_ID = 2L;

    @MockBean
    MemberService memberService;

    @MockBean
    FavoriteService favoriteService;

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

    @DisplayName("즐겨찾기에 해당 경로가 등록되어있는지 확인")
    @Test
    void isExistFavoritePath() throws Exception {
        given(favoriteService.hasFavoritePath(any(), any(), any())).willReturn(new FavoriteExistenceResponse(true));
        String expected = "{\"existence\" : " + true + "}";

        this.mockMvc.perform(
            get("/me/favorites/source/{sourceId}/target/{targetId}/existsPath", SOURCE_STATION_ID, TARGET_STATION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andDo(print())
            .andDo(MeDocumentation.getFavoriteByPath())
        ;
    }

    @DisplayName("즐겨찾기에 경로 추가")
    @Test
    void addFavorite() throws Exception {
        String body =
            "{\"sourceStationId\" : " + SOURCE_STATION_ID + ", \"targetStationId\" : " + TARGET_STATION_ID + "}";

        this.mockMvc.perform(post("/me/favorites")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(MeDocumentation.addFavorite())
        ;
    }

    @DisplayName("즐겨찾기에서 경로 제거")
    @Test
    void removeFavorite() throws Exception {
        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/me/favorites/source/{sourceId}/target/{targetId}",
                SOURCE_STATION_ID, TARGET_STATION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(MeDocumentation.removeFavorite())
        ;
    }

    @DisplayName("즐겨찾기 전체 목록을 조회.")
    @Test
    void getFavorites() throws Exception {
        LocalDateTime createdTime = LocalDateTime.of(2020, 5, 26, 14, 18, 18);
        StationResponse gangnam = new StationResponse(1L, "강남", createdTime);
        StationResponse gangbuk = new StationResponse(2L, "강북", createdTime);
        StationResponse gangdong = new StationResponse(3L, "강동", createdTime);

        List<FavoriteResponse> favoriteResponses = Arrays.asList(new FavoriteResponse(gangbuk, gangdong),
            new FavoriteResponse(gangbuk, gangnam), new FavoriteResponse(gangnam, gangdong));

        String expected = "[{\"sourceStation\":{\"id\":2,\"name\":\"강북\",\"createdAt\":\"2020-05-26T14:18:18\"},"
            + "\"targetStation\":{\"id\":3,\"name\":\"강동\",\"createdAt\":\"2020-05-26T14:18:18\"}},"
            + "{\"sourceStation\":{\"id\":2,\"name\":\"강북\",\"createdAt\":\"2020-05-26T14:18:18\"},"
            + "\"targetStation\":{\"id\":1,\"name\":\"강남\",\"createdAt\":\"2020-05-26T14:18:18\"}},"
            + "{\"sourceStation\":{\"id\":1,\"name\":\"강남\",\"createdAt\":\"2020-05-26T14:18:18\"},"
            + "\"targetStation\":{\"id\":3,\"name\":\"강동\",\"createdAt\":\"2020-05-26T14:18:18\"}}]";

        when(favoriteService.getFavorites(any())).thenReturn(favoriteResponses);
        this.mockMvc.perform(get("/me/favorites")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andDo(MeDocumentation.getFavorites())
        ;
    }
}
