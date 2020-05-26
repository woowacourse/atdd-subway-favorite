package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    /*
     * Scenario: 즐겨찾기를 관리한다.
     *  Given: 회원가입한다.
     *  And: 로그인 요청한다.
     *  And: 지하철 역이 등록되어 있다.
     *  And: 지하철 노선이 등록되어 있다.
     *  And: 지하철 경로가 등록되어 있다.
     *
     * 강남 - 역삼 - 선릉
     * |           |
     * |          한티
     * |           |
     * 양재 - 매봉 - 도곡
     *
     *  Given: 지하철 경로를 검색한다.
     *  When: 즐겨찾기에 등록한다.
     *  Then: 즐겨찾기에 등록되었다.
     *
     *  When: 즐겨찾기를 조회한다.
     *  Then: 즐겨찾기 목록이 출력된다.
     *
     *  When: 즐겨찾기를 삭제한다.
     *  Then: 즐겨찾기가 삭제되었다.
     */
    @Test
    void manageFavorite() {
        join(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        initStation();

        findPath(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG, "DISTANCE");
        createFavorite(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG, token);
        createFavorite(STATION_NAME_HANTI, STATION_NAME_DOGOK, token);
        List<FavoriteResponse> favoriteResponse = getFavorites(token);

        assertThat(favoriteResponse).hasSize(2);
        assertThat(favoriteResponse.get(0).getSource()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(favoriteResponse.get(0).getTarget()).isEqualTo(STATION_NAME_SEOLLEUNG);

        deleteFavorite(favoriteResponse.get(0).getId(), token);
        List<FavoriteResponse> deletedResponse = getFavorites(token);
        assertThat(deletedResponse).hasSize(1);
    }

    private void createFavorite(String source, String target, TokenResponse token) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        given()
                .auth().oauth2(token.getAccessToken())
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/me/favorites")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    private List<FavoriteResponse> getFavorites(TokenResponse token) {
        return given()
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/me/favorites")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", FavoriteResponse.class);
    }

    private void deleteFavorite(Long id, TokenResponse token) {
        given()
                .auth().oauth2(token.getAccessToken())
                .pathParam("id", id)
                .when()
                .delete("/me/favorites/{id}")
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
