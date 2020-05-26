package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String AUTHORIZATION = "Authorization";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        initStation();
    }

    /**
     * Feature: 경로 즐겨찾기 기능
     * <p>
     * Scenario: 경로 즐겨찾기를 추가하고 조회하고 삭제한다.
     * Given 지하철역이 여러 개 추가되어있다.
     * And 지하철 노선이 여러 개 추가되어있다.
     * And 지하철 노선에 지하철역이 여러 개 추가되어있다.
     * And 사용자가 로그인이 되어 있다.
     * <p>
     * When 즐겨찾기를 n개 추가 요청을 한다.
     * And 즐겨찾기를 목록을 조회 요청을 한다.
     * Then 즐겨찾기 목록을 응답받는다.
     * Then 즐겨찾기 목록은 n개이다.
     * <p>
     * When 즐겨찾기를 삭제 요청을 한다.
     * And 즐겨찾기를 목록을 조회 요청을 한다.
     * Then 즐겨찾기 목록을 응답받는다.
     * Then 즐겨찾기 목록은 n-1개이다.
     */
    @Test
    void manageFavorite() {
        //And
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String token = tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken();

        //when
        addFavorite(token, "강남역", "도곡역");
        addFavorite(token, "강남역", "매봉역");
        addFavorite(token, "강남역", "한티역");

        //and
        List<FavoriteResponse> favorites = getFavorites(token);
        //then
        assertThat(favorites.size()).isEqualTo(3);

        deleteFavorite(token, 1L);

       favorites = getFavorites(token);
       assertThat(favorites.size()).isEqualTo(2);
    }

    private void deleteFavorite(String token, Long favoriteId) {
        given().header(AUTHORIZATION, token)
                .when()
                .delete("/members/favorites/" + favoriteId)
                .then()
                .log().all().statusCode(HttpStatus.NO_CONTENT.value());
    }

    private List<FavoriteResponse> getFavorites(String token) {
        return given().header(AUTHORIZATION, token).
                when().
                get("/members/favorites").
                then().
                log().all().statusCode(HttpStatus.OK.value()).
                extract().
                jsonPath().getList(".", FavoriteResponse.class);
    }

    private void addFavorite(String token, String source, String target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        given().header(AUTHORIZATION, token).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members/favorites").
                then().
                log().all().statusCode(HttpStatus.OK.value());
    }
}
