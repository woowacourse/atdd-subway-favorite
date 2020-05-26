package wooteco.subway.web.favorite;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.service.member.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerIntegrationTest {
    private static final Gson gson = new Gson();
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        favoriteRepository.deleteAll();
    }

    @DisplayName("JWT 토큰이 없는 즐겨찾기 추가 요청")
    @Test
    void createFavoriteFail1() throws Exception {
        String source = "강남역";
        String target = "한티역";
        CreateFavoriteRequest createFavoriteRequest = new CreateFavoriteRequest(source, target);

        String uri = "/favorites";
        String content = gson.toJson(createFavoriteRequest);

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

        String uri = "/favorites";
        String content = gson.toJson(createFavoriteRequest);

        mockMvc.perform(post(uri)
                .header("Authorization", "Bearer " + wrongRequestToken)
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
        Favorite favorite1 = new Favorite("강남역", "양재역", DummyTestUserInfo.EMAIL);
        Favorite favorite2 = new Favorite("양재역", "판교역", DummyTestUserInfo.EMAIL);
        favoriteService.save(favorite1);
        favoriteService.save(favorite2);
        Member member = new Member(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        memberService.createMember(member);
        String token = jwtTokenProvider.createToken(member.getEmail());
        String uri = "/favorites";

        //when
        MvcResult mvcResult = mockMvc.perform(get(uri)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        FavoritesResponse favoritesResponse = gson.fromJson(mvcResult.getResponse().getContentAsString(), FavoritesResponse.class);
        assertThat(favoritesResponse.getFavoriteResponses()).hasSize(2);
    }

    @DisplayName("토큰이 없는 상태에서 즐겨찾기 조회가 실패한다")
    @Test
    void findFavoriteFailTest() throws Exception {

        Favorite favorite1 = new Favorite("강남역", "양재역", DummyTestUserInfo.EMAIL);
        Favorite favorite2 = new Favorite("양재역", "판교역", DummyTestUserInfo.EMAIL);
        favoriteService.save(favorite1);
        favoriteService.save(favorite2);

        String uri = "/favorites";

        mockMvc.perform(get(uri)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("회원이 아닌 이메일 JWT 토큰 헤더 요청 시 즐겨찾기 조회 요청이 실패 한다")
    @Test
    void findFavoriteFailTest2() throws Exception {

        Favorite favorite1 = new Favorite("강남역", "양재역", DummyTestUserInfo.EMAIL);
        Favorite favorite2 = new Favorite("양재역", "판교역", DummyTestUserInfo.EMAIL);
        favoriteService.save(favorite1);
        favoriteService.save(favorite2);

        String unExistEmailToken = jwtTokenProvider.createToken("wrong@gmail.com");
        String uri = "/favorites";

        mockMvc.perform(get(uri)
                .header("Authorization", "Bearer" + unExistEmailToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
