package wooteco.subway.web.favorite;

import com.google.gson.Gson;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.service.member.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
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
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
        favoriteRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("JWT 토큰이 없는 즐겨찾기 추가 요청")
    @Test
    void createFavoriteFail1() throws Exception {
        String source = "강남역";
        String target = "한티역";
        CreateFavoriteRequest createFavoriteRequest = new CreateFavoriteRequest(source, target, DummyTestUserInfo.EMAIL);

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

        CreateFavoriteRequest createFavoriteRequest = new CreateFavoriteRequest(source, target, DummyTestUserInfo.EMAIL);

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
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역", DummyTestUserInfo.EMAIL);
        CreateFavoriteRequest favorite2 = new CreateFavoriteRequest("양재역", "판교역", DummyTestUserInfo.EMAIL);
        Member member = new Member(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        favoriteService.save(favorite1, member.getEmail());
        favoriteService.save(favorite2, member.getEmail());
        memberService.createMember(member);
        String token = jwtTokenProvider.createToken(member.getEmail());
        String uri = "/favorites";

        //when
        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(FavoriteDocumentation.select())
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

        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역", DummyTestUserInfo.EMAIL);
        CreateFavoriteRequest favorite2 = new CreateFavoriteRequest("양재역", "판교역", DummyTestUserInfo.EMAIL);
        favoriteService.save(favorite1, DummyTestUserInfo.EMAIL);
        favoriteService.save(favorite2, DummyTestUserInfo.EMAIL);

        String uri = "/favorites";

        mockMvc.perform(get(uri)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("회원이 아닌 이메일 JWT 토큰 헤더 요청 시 즐겨찾기 조회 요청이 실패 한다")
    @Test
    void findFavoriteFailTest2() throws Exception {
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역", DummyTestUserInfo.EMAIL);
        CreateFavoriteRequest favorite2 = new CreateFavoriteRequest("양재역", "판교역", DummyTestUserInfo.EMAIL);
        favoriteService.save(favorite1, DummyTestUserInfo.EMAIL);
        favoriteService.save(favorite2, DummyTestUserInfo.EMAIL);

        String unExistEmailToken = jwtTokenProvider.createToken("wrong@gmail.com");
        String uri = "/favorites";

        mockMvc.perform(get(uri)
                .header("Authorization", "Bearer" + unExistEmailToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("즐겨찾기 삭제가 성공한다")
    @Test
    void deleteFavoriteSuccessTest() throws Exception {
        //given
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역", DummyTestUserInfo.EMAIL);
        Favorite present = favoriteService.save(favorite1, DummyTestUserInfo.EMAIL);
        Member member = new Member(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        memberService.createMember(member);

        String token = jwtTokenProvider.createToken(member.getEmail());
        String uri = "/favorites/" + present.getId();

        //when
        mockMvc.perform(delete(uri)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("토큰이 없는 상태에서 즐겨찾기 삭제가 실패한다")
    @Test
    void deleteFavoriteFailTest() throws Exception {
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역", DummyTestUserInfo.EMAIL);
        Favorite present = favoriteService.save(favorite1, favorite1.getEmail());

        Member member = new Member(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        memberService.createMember(member);

        favoriteService.save(favorite1, member.getEmail());

        String uri = "/favorites/" + present.getId();

        mockMvc.perform(delete(uri)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("회원이 아닌 이메일 JWT 토큰 헤더 요청 시 즐겨찾기 삭제 요청이 실패 한다")
    @Test
    void deleteFavoriteFailTest2() throws Exception {
        CreateFavoriteRequest favorite1 = new CreateFavoriteRequest("강남역", "양재역", DummyTestUserInfo.EMAIL);
        Favorite present = favoriteService.save(favorite1, DummyTestUserInfo.EMAIL);

        Member member = new Member(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        memberService.createMember(member);

        favoriteService.save(favorite1, member.getEmail());

        String unExistEmailToken = jwtTokenProvider.createToken("wrong@gmail.com");
        String uri = "/favorites/?source=" + present.getSource() + "&target=" + present.getTarget();


        mockMvc.perform(get(uri)
                .header("Authorization", "Bearer" + unExistEmailToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
