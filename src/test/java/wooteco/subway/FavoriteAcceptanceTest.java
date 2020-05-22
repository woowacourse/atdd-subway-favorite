package wooteco.subway;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철 노선도 즐겨찾기 기능에 관한 인수테스트")
    void favorite() {
        /**
         * Feature: 지하철 노선 즐겨찾기 기능
         * Scenario: 지하철 노선 즐겨찾기 기능을 사용한다
         *
         * Given 로그인이 되어있다
         * When 경로에 즐겨찾기 추가를 한다
         * Then 즐겨찾기에 경로가 추가된다
         *
         * Given 로그인이 되어있고, 즐겨찾기에 경로가 추가되어 있다
         * When 즐겨찾기 조회한다
         * Then 즐겨찾기가 조회된다
         *
         * Given 즐겨찾기에 등록이 되어있다
         * When 즐겨찾기에 등록되어있는 경로의 삭제 요청을 한다
         * Then 등록된 즐겨찾기 경로가 삭제된다
         */
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        final TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(1L, 5L);
        addFavorite(tokenResponse, favoriteCreateRequest);

        final List<FavoriteResponse> favorite = getFavorite(tokenResponse);
        assertThat(favorite.size()).isEqualTo(1);
        assertThat(favorite.get(0)).isNotNull();

        deleteFavorite(tokenResponse, favorite.get(0).getId());
        assertThat(getFavorite(tokenResponse).size()).isEqualTo(0);
    }

    void addFavorite(TokenResponse token, FavoriteCreateRequest favoriteCreateRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("sourceStationId", String.valueOf(favoriteCreateRequest.getSourceStationId()));
        params.put("targetStationId", String.valueOf(favoriteCreateRequest.getTargetStationId()));

        given()
            .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
            .body(params)
            .when()
            .post("/favorites")
            .then()
            .log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    void deleteFavorite(TokenResponse token, Long id) {
        given()
            .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
            .when()
            .delete("/favorites/" + id)
            .then()
            .log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    List<FavoriteResponse> getFavorite(TokenResponse token) {
        return given()
            .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
            .when()
            .get("/favorites")
            .then()
            .log().all()
            .extract().jsonPath().getList(".", FavoriteResponse.class);
    }
}
