package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.web.member.util.AuthorizationExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    FavoriteRepository favoriteRepository;

    @BeforeEach
    public void setUp() {
        favoriteRepository.deleteAll();
    }

    @DisplayName("즐겨 찾기 추가/삭제/조회")
    @Test
    void favoriteAcceptanceTest() {
        /**
         * 강남 - 역삼 - 선릉
         * |           |
         * |          한티
         * |           |
         * 양재 - 매봉 - 도곡
         */
        String token = jwtTokenProvider.createToken(DummyTestUserInfo.EMAIL);
        //Given
        // 지하철과 노선, 간선이 주어진다.
        initStation();

        //When
        // 검색한 경로를 즐겨찾기에 추가하는 요청을 보낸다.
        createMember(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD,token);
        createFavorite("강남역", "한티역", token);
        createFavorite("매봉역", "도곡역", token);

        //Then
        // 즐겨찾기에 추가된다.
        FavoritesResponse favoritesResponse = getAllFavorite(token);
        List<FavoriteResponse> favoriteResponses = favoritesResponse.getFavoriteResponses();
        assertThat(favoriteResponses).hasSize(2);
        //assertThat(favoritesResponse.getSize()).isEqualTo(2);
        //When
        // 즐겨 찾기 목록중 하나를 삭제 한다.
        FavoriteResponse favoriteResponse = favoriteResponses.get(1);
        deleteFavorite(favoriteResponse, token);
        //Then
        // 즐겨 찾기 목록이 하나 줄어든다.
        favoritesResponse = getAllFavorite(token);
        assertThat(favoritesResponse.getFavoriteResponses()).hasSize(1);
    }

    private void deleteFavorite(FavoriteResponse favoriteResponse, String token) {
        System.out.println(favoriteResponse.getId());
        given().
                header(AuthorizationExtractor.AUTHORIZATION, "Bearer " + token).
                when().
                delete("/auth/favorites/" + favoriteResponse.getId()).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());

    }

    private FavoritesResponse getAllFavorite(String token) {
        return given().
                header("Authorization", "Bearer " + token).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/auth/favorites").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(FavoritesResponse.class);

    }

    private String createFavorite(String source, String target, String token) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return given().
                header("Authorization", "Bearer " + token).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/auth/favorites").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().header("Favorite");
    }
}
