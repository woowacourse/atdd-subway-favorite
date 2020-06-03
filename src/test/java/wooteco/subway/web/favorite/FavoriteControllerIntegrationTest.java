package wooteco.subway.web.favorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.util.AuthorizationExtractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.DummyTestUserInfo.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/truncate.sql")
public class FavoriteControllerIntegrationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
        favoriteRepository.deleteAll();
        memberRepository.deleteAll();
        stationRepository.save(new Station("강남역"));
        stationRepository.save(new Station("양재역"));
        stationRepository.save(new Station("판교역"));
    }

    @DisplayName("JWT 토큰이 없는 즐겨찾기 추가 요청")
    @Test
    void createFavoriteFail1() throws Exception {
        String source = "강남역";
        String target = "한티역";
        CreateFavoriteRequest createFavoriteRequest = new CreateFavoriteRequest(source, target);

        String uri = "/auth/favorites";

        String content = OBJECT_MAPPER.writeValueAsString(createFavoriteRequest);

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("회원이 아닌 이메일 JWT 토큰 헤더 요청 시 즐겨찾기 추가 요청")
    @Test
    void createFavoriteFail2() throws Exception {
        String source = "강남역";
        String target = "한티역";

        CreateFavoriteRequest createFavoriteRequest = new CreateFavoriteRequest(source, target);

        String wrongRequestEmail = "email@gmail.com";
        String wrongRequestToken = jwtTokenProvider.createToken(wrongRequestEmail);

        String uri = "/auth/favorites";

        String content = OBJECT_MAPPER.writeValueAsString(createFavoriteRequest);

        mockMvc.perform(post(uri)
                .header(AuthorizationExtractor.AUTHORIZATION, "Bearer " + wrongRequestToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("즐겨찾기 조회가 성공한다")
    @Test
    void findFavoriteSuccessTest() throws Exception {
        //given
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역");
        CreateFavoriteRequest favorite2 = new CreateFavoriteRequest("양재역", "판교역");

        Member member = memberService.createMember(new Member(EMAIL, NAME, PASSWORD));
        favoriteService.createFavorite(member, favorite1);
        favoriteService.createFavorite(member, favorite2);


        String token = jwtTokenProvider.createToken(member.getEmail());
        String uri = "/auth/favorites";

        //when
        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.get(uri)
                .header(AuthorizationExtractor.AUTHORIZATION, "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(FavoriteDocumentation.select())
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        FavoritesResponse favoritesResponse =
                OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), FavoritesResponse.class);
        assertThat(favoritesResponse.getFavoriteResponses()).hasSize(2);
    }

    @DisplayName("토큰이 없는 상태에서 즐겨찾기 조회가 실패한다")
    @Test
    void findFavoriteFailTest() throws Exception {
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역");
        CreateFavoriteRequest favorite2 = new CreateFavoriteRequest("양재역", "판교역");
        Member member = memberRepository.save(new Member(EMAIL, NAME, PASSWORD));

        favoriteService.createFavorite(member, favorite1);
        favoriteService.createFavorite(member, favorite2);

        String uri = "/auth/favorites";

        mockMvc.perform(get(uri)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("회원이 아닌 이메일 JWT 토큰 헤더 요청 시 즐겨찾기 조회 요청이 실패 한다")
    @Test
    void findFavoriteFailTest2() throws Exception {
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역");
        CreateFavoriteRequest favorite2 = new CreateFavoriteRequest("양재역", "판교역");
        Member member = new Member(1L, EMAIL, NAME, PASSWORD);
        favoriteService.createFavorite(member, favorite1);
        favoriteService.createFavorite(member, favorite2);

        String unExistEmailToken = jwtTokenProvider.createToken("wrong@gmail.com");
        String uri = "/auth/favorites";

        mockMvc.perform(get(uri)
                .header(AuthorizationExtractor.AUTHORIZATION, "Bearer" + unExistEmailToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("즐겨찾기 삭제가 성공한다")
    @Test
    void deleteFavoriteSuccessTest() throws Exception {
        //given
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역");
        Member member = memberService.createMember(new Member(EMAIL, NAME, PASSWORD));
        FavoriteResponse createdFavorites = favoriteService.createFavorite(member, favorite1);

        String token = jwtTokenProvider.createToken(member.getEmail());

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/auth/favorites/{id}", createdFavorites.getId())
                .header(AuthorizationExtractor.AUTHORIZATION, "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(FavoriteDocumentation.delete())
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("토큰이 없는 상태에서 즐겨찾기 삭제가 실패한다")
    @Test
    void deleteFavoriteFailTest() throws Exception {
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역");
        Member member = memberService.createMember(new Member(EMAIL, NAME, PASSWORD));
        FavoriteResponse createdFavorites = favoriteService.createFavorite(member, favorite1);

        String uri = "/auth/favorites/" + createdFavorites.getId();

        mockMvc.perform(delete(uri)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("회원이 아닌 이메일 JWT 토큰 헤더 요청 시 즐겨찾기 삭제 요청이 실패 한다")
    @Test
    void deleteFavoriteFailTest2() throws Exception {
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역");
        Member member = memberService.createMember(new Member(EMAIL, NAME, PASSWORD));
        FavoriteResponse createdFavorites = favoriteService.createFavorite(member, favorite1);

        String unExistEmailToken = jwtTokenProvider.createToken("wrong@gmail.com");
        String uri = "/auth/favorites/" + createdFavorites.getId();

        mockMvc.perform(delete(uri)
                .header(AuthorizationExtractor.AUTHORIZATION, "Bearer" + unExistEmailToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
