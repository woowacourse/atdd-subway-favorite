package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

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
        //Given
        // 지하철과 노선, 간선이 주어진다.
        initStation();
        //When
        // 검색한 경로를 즐겨찾기에 추가하는 요청을 보낸다.
        createMember(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        createFavorite("강남역", "한티역");
        createFavorite("매봉역", "도곡역");
        //Then
        // 즐겨찾기에 추가된다.
        FavoritesResponse favoritesResponse = getAllFavorite();
        List<FavoriteResponse> favoriteResponses = favoritesResponse.getFavoriteResponses();
        assertThat(favoriteResponses).hasSize(2);
        //When
        // 즐겨 찾기 목록중 하나를 삭제 한다.
        FavoriteResponse favoriteResponse = favoriteResponses.get(0);
        deleteFavorite(favoriteResponse);
        //Then
        // 즐겨 찾기 목록이 하나 줄어든다.
        assertThat(favoritesResponse.getFavoriteResponses()).hasSize(1);
    }

    private void deleteFavorite(FavoriteResponse favoriteResponse) {
        given().when().
                delete("/favorites?source=" + favoriteResponse.getSource() + "target=" + favoriteResponse.getTarget()).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());

    }

    private FavoritesResponse getAllFavorite() {
        return given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/favorites").
                then().
                log().all()
                .statusCode(HttpStatus.OK.value()).
                        extract().as(FavoritesResponse.class);

    }

    private String createFavorite(String source, String target) {
        String token = jwtTokenProvider.createToken(DummyTestUserInfo.EMAIL);

        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return given().
                header("Authorization", "Bearer " + token).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/favorites").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().header("Favorite");
    }
}
