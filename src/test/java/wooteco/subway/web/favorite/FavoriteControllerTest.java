package wooteco.subway.web.favorite;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.acceptance.AcceptanceTest.*;
import static wooteco.subway.web.member.MemberControllerTest.*;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.exceptions.ControllerExceptionHandler;
import wooteco.subway.web.member.AuthorizationExtractor;

@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(ControllerExceptionHandler.class)
class FavoriteControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthorizationExtractor authorizationExtractor;

    private Member member;

    @BeforeEach
    void setUp(
        WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(authorizationExtractor.extract(any(), anyString())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("즐겨찾기 추가-인증")
    @Test
    public void createFavorite() throws Exception {
        Favorite favorite = new Favorite(1L, member.getId(), 1L, 2L);

        given(favoriteService.createFavorite(any(), any())).willReturn(
            FavoriteResponse.from(favorite));
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(post("/favorites")
            .header(TEST_AUTHORIZATION, TEST_USER_TOKEN)
            .content("{\"source\":1, \"target\":2}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(FavoriteDocumentation.createFavorite());

        verify(favoriteService).createFavorite(eq(1L), any());
    }

    @DisplayName("즐겨찾기 추가-미인증")
    @Test
    void createFavoriteInvalidAuthentication() throws Exception {
        given(jwtTokenProvider.getSubject(anyString())).willReturn("outdated");

        mockMvc.perform(post("/favorites")
            .header(TEST_AUTHORIZATION, TEST_OUTDATED_TOKEN)
            .content("{\"source\":1, \"target\":2}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(print())
            .andDo(FavoriteDocumentation.createFavoriteWithoutAuthentication());
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() throws Exception {
        List<FavoriteResponse> favorites = new ArrayList<>();
        Favorite favorite = new Favorite(1L, member.getId(), 1L, 2L);
        favorites.add(FavoriteResponse.of(favorite, "강남역", "삼성역"));

        given(favoriteService.getFavorites(any())).willReturn(favorites);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(get("/favorites")
            .header(TEST_AUTHORIZATION, TEST_USER_TOKEN))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoriteDocumentation.getFavorites());
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorites() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);

        mockMvc.perform(delete("/favorites/" + 10L)
            .header(TEST_AUTHORIZATION, TEST_USER_TOKEN))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(FavoriteDocumentation.deleteFavorite());

        verify(favoriteService).deleteFavorite(10L);
    }
}