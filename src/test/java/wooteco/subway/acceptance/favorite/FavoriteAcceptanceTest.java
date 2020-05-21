package wooteco.subway.acceptance.favorite;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.acceptance.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.web.dto.ExceptionResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    /**
     * Scenario: 즐겨찾기 추가/조회/삭제 기능을 구현한다.
     * <p>
     * given 로그인이 되어있지 않다.
     * and 즐겨찾기할 역이 주어져있다.
     * when 즐겨찾기에 등록한다.
     * then 즐겨찾기가 실패한다.
     * <p>
     * when 즐겨찾기 된 역들을 조회한다.
     * then 조회가 실패한다.
     * <p>
     * when 즐겨찾기 된 역들을 삭제한다.
     * then 삭제가 실패한다.
     * <p>
     * given 로그인이 되어있다.
     * when 즐겨찾기에 등록한다.
     * then 즐겨찾기가 성공한다.
     * <p>
     * when 즐겨찾기 경로들을 조회한다.
     * then 즐겨찾기 경로들이 조회된다.
     * <p>
     * when 즐겨찾기 경로를 삭제한다.
     * then 즐겨찾기 경로가 삭제된다.
     * <p>
     * given 등록되지 않은 역이 주어진다.
     * when 즐겨찾기에 등록한다.
     * then 즐겨찾기가 실패한다.
     * <p>
     * given 존재하지 않는 즐겨찾기 경로가 주어진다.
     * when 존재하지 않는 즐겨찾기 경로를 삭제한다.
     * then 삭제가 실패한다.
     */

    @DisplayName("즐겨찾기 추가/조회/삭제")
    @Test
    void favoriteManage() {
        initStation();
        createMemberSuccessfully(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD, TEST_USER_PASSWORD);
        ExceptionResponse notAuthorizedUserResponse =
                registerFavoritePathFailed("", STATION_NAME_KANGNAM, STATION_NAME_HANTI, HttpStatus.UNAUTHORIZED.value());
        assertThat(notAuthorizedUserResponse.getErrorMessage()).isEqualTo("유효하지 않은 토큰입니다!");

        notAuthorizedUserResponse = retrieveFavoritePathFailed("");
        assertThat(notAuthorizedUserResponse.getErrorMessage()).isEqualTo("유효하지 않은 토큰입니다!");

        notAuthorizedUserResponse = deleteFavoritePathFailed("", 1L, HttpStatus.UNAUTHORIZED.value());
        assertThat(notAuthorizedUserResponse.getErrorMessage()).isEqualTo("유효하지 않은 토큰입니다!");

        TokenResponse tokenResponse = loginSuccessfully(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String authorization = tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken();

        registerFavoritePathSuccessfully(authorization, STATION_NAME_KANGNAM, STATION_NAME_HANTI);

        FavoritePathResponse response = retrieveFavoritePathSuccessfully(authorization);
        assertThat(response.getFavoritePaths()).hasSize(1);
        assertThat(response.getFavoritePaths().get(0).getSource().getName()).isEqualTo(STATION_NAME_KANGNAM);

        deleteFavoritePathSuccessfully(authorization, response.getFavoritePaths().get(0).getId());
        response = retrieveFavoritePathSuccessfully(authorization);
        assertThat(response.getFavoritePaths()).hasSize(0);

        ExceptionResponse stationNotExistResponse =
                registerFavoritePathFailed(authorization, STATION_NAME_KANGNAM, "NOT_REGISTERED_STATION", HttpStatus.BAD_REQUEST.value());
        assertThat(stationNotExistResponse.getErrorMessage()).isEqualTo("NOT_REGISTERED_STATION" + "역이 존재하지 않습니다.");

        ExceptionResponse notRegisteredPathResponse = deleteFavoritePathFailed(authorization, 1L, HttpStatus.BAD_REQUEST.value());
        assertThat(notRegisteredPathResponse.getErrorMessage()).isEqualTo(String.format("Id가 %d인 즐겨찾기 경로가 존재하지 않습니다.", 1L));
    }

    private ExceptionResponse retrieveFavoritePathFailed(String authorization) {
        return retrieveFavoritePath(authorization)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract().as(ExceptionResponse.class);
    }

    private FavoritePathResponse retrieveFavoritePathSuccessfully(String authorization) {
        return retrieveFavoritePath(authorization)
                .statusCode(HttpStatus.OK.value())
                .extract().as(FavoritePathResponse.class);
    }

    private ExceptionResponse registerFavoritePathFailed(String authorization, String source, String target, int statusCode) {
        return registerFavoritePath(authorization, source, target)
                .statusCode(statusCode)
                .extract().as(ExceptionResponse.class);
    }

    private void registerFavoritePathSuccessfully(String authorization, String source, String target) {
        registerFavoritePath(authorization, source, target)
                .statusCode(HttpStatus.CREATED.value());
    }

    private ExceptionResponse deleteFavoritePathFailed(String authorization, long pathId, int statusCode) {
        return deleteFavoritePath(authorization, pathId)
                .statusCode(statusCode)
                .extract().as(ExceptionResponse.class);
    }

    private void deleteFavoritePathSuccessfully(String authorization, Long pathId) {
        deleteFavoritePath(authorization, pathId)
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private ValidatableResponse registerFavoritePath(String authorization, String source, String target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        //@formatter:off
        return
                given().
                        header("Authorization", authorization).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        body(params).
                when().
                        post("/me/favorite").
                then().
                        log().all();
        //@formatter:on
    }

    private ValidatableResponse retrieveFavoritePath(String authorization) {
        //@formatter:off
        return
                given().
                        header("Authorization", authorization).
                when().
                        get("/me/favorite").
                then().
                        log().all();
        //@formatter:on
    }

    private ValidatableResponse deleteFavoritePath(String authorization, Long pathId) {
        //@formatter:off
        return
                given().
                        header("Authorization", authorization).
                when().
                        delete("/me/favorite/" + pathId).
                then().
                        log().all();
        //@formatter:on
    }
}
