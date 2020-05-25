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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
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

    @DisplayName("권한 없는 즐겨찾기 추가 요청")
    @Test
    void createFavoriteFail2() throws Exception {
        String source = "강남역";
        String target = "한티역";
        String memberEmail = "ramen6315@gmail.com";
        Favorite favorite = favoriteService.createFavorite(source, target, memberEmail);
        System.out.println(favorite);

        CreateFavoriteRequest createFavoriteRequest = new CreateFavoriteRequest(source, target);

        String wrongRequestEmail = "email@gmail.com";
        String wrongRequestToken = jwtTokenProvider.createToken(wrongRequestEmail);

        String uri = "/favorites";
        String content = gson.toJson(createFavoriteRequest);
        System.out.println(content + " contnenenenenen");

        mockMvc.perform(post(uri)
                .header("Authorization", "Bearer " + wrongRequestToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
