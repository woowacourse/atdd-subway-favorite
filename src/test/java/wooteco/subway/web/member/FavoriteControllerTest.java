package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.FavoriteController;
import wooteco.subway.web.member.login.BearerAuthInterceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public class FavoriteControllerTest {

    @MockBean
    private FavoriteService favoriteService;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("본인의 모든 즐겨찾기 목록을 요청했을때 OK 응답이 오는지 테스트")
    @Test
    void getFavoritesTest() throws Exception {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        FavoriteResponse response = new FavoriteResponse(1L, 1L, 2L, STATION_NAME_KANGNAM, STATION_NAME_DOGOK);

        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(favoriteService.findAllFavoriteResponses(any())).willReturn(Arrays.asList(response));

        this.mockMvc.perform(get("/me/favorites")
                .header("Authorization", "Bearer " + TEST_TOKEN)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FavoriteDocumentation.getFavorites());
    }

    @DisplayName("본인의 계정에 즐겨찾기를 추가했을때 OK 응답이 오는지 테스트")
    @Test
    void addFavoriteTest() throws Exception {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        doNothing().when(favoriteService).createFavorite(any(), any());

        String inputJson = "{\"sourceStationName\":\"" + STATION_NAME_KANGNAM + "\"," +
                "\"targetStationName\":\"" + STATION_NAME_DOGOK + "\"}";

        this.mockMvc.perform(post("/me/favorites")
                .header("Authorization", "Bearer " + TEST_TOKEN)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FavoriteDocumentation.addFavorite());
    }

    @DisplayName("즐겨찾기 id로 즐겨찾기를 삭제했을때 OK응답이 오는지 테스트")
    @Test
    void deleteFavoriteTest() throws Exception {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        doNothing().when(favoriteService).deleteFavorite(any());

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/me/favorites/{id}", 1L)
                .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FavoriteDocumentation.deleteFavorite());
    }
}
