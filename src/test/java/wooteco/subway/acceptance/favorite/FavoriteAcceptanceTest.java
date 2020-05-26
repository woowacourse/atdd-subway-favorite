package wooteco.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteExistenceResponse;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    /* Feature: 즐겨 찾기 관리
     *
     * Scenario : 즐겨 찾기를 관리 한다.
     *
     * Given 역이 추가되어 있다.
     *       노선이 추가되어 있다.
     *       노선에 역이 추가되어 있다.
     *       회원을 등록 한다. /조회/
     *       등록한 회원으로 로그인을 한다.
     * When 즐겨찾기에 역삼역>한티역 경로가 등록되어 있는지 확인한다.
     * Then 해당 경로가 즐겨찾기가 등록되어 있지 않다.
     *
     * When 경로를 즐겨찾기에 추가한다. /추가/
     * Then 즐겨찾기를 경로로 조회한다.
     *      즐겨찾기에 추가되어 있다.
     *
     * When 즐겨찾기를 경로로 조회한다. /추가 조회/
     * Then 즐겨찾기는 추가되어있지 않다.
     *
     * When 경로를 즐겨 찾기에서 제외한다. /삭제/
     * Then 즐겨 찾기를 경로로 조회한다.
     *      즐겨 찾기는 추가되어있지 않다.
     *
     * Given 경로를 즐겨 찾기에 3개 추가 한다.
     * When 즐겨 찾기 경로를 모두 조회 한다.
     * Then 즐겨 찾기에 경로가 3개이다.
     *
     * When 즐겨 찾기 목록에서 경로를 삭제 한다.
     * Then 즐겨 찾기 경로 개수가 2개이다.
     *
     * When 즐겨 찾기를 삭제한 경로로 조회 한다. /삭제 조회/
     * Then 즐겨 찾기는 추가 되어 있지 않다.
     */
    @Test
    void manageFavorite() {
        LineResponse lineTwo = createLine(LINE_NAME_2);
        StationResponse stationKangnam = createStation(STATION_NAME_KANGNAM);
        StationResponse stationYeoksam = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationHanti = createStation(STATION_NAME_HANTI);
        addLineStation(lineTwo.getId(), null, stationKangnam.getId());
        addLineStation(lineTwo.getId(), stationKangnam.getId(), stationYeoksam.getId());
        addLineStation(lineTwo.getId(), stationYeoksam.getId(), stationHanti.getId());

        createMember(TIGER_EMAIL, TIGER_NAME, TIGER_PASSWORD);
        TokenResponse loginToken = login(TIGER_EMAIL, TIGER_PASSWORD);
        FavoriteExistenceResponse favoriteExistenceResponse = existFavorite(loginToken, stationKangnam.getId(),
            stationHanti.getId());
        // 구현시 False로 바꿀 것
        assertThat(favoriteExistenceResponse.isExistence()).isTrue();

        addFavorite(loginToken, stationYeoksam.getId(), stationHanti.getId());
        favoriteExistenceResponse = existFavorite(loginToken, stationKangnam.getId(), stationHanti.getId());
        assertThat(favoriteExistenceResponse.isExistence()).isTrue();

        removeFavorite(loginToken, stationYeoksam.getId(), stationHanti.getId());
        favoriteExistenceResponse = existFavorite(loginToken, stationKangnam.getId(), stationHanti.getId());
        // TODO: 2020/05/26 추후 false로 변경 해야함
        assertThat(favoriteExistenceResponse.isExistence()).isTrue();

        //given
        addFavorite(loginToken, stationYeoksam.getId(), stationHanti.getId());
        addFavorite(loginToken, stationKangnam.getId(), stationHanti.getId());
        addFavorite(loginToken, stationYeoksam.getId(), stationKangnam.getId());
        List<FavoriteResponse> allFavorites = findAllFavorites(loginToken);

        // TODO: 2020/05/26 추후 size 3으로 변경
        assertThat(allFavorites).hasSize(0);

        removeFavorite(loginToken, stationYeoksam.getId(), stationHanti.getId());
        allFavorites = findAllFavorites(loginToken);
        // TODO: 2020/05/26 추후 size 2로 변경
        assertThat(allFavorites).hasSize(0);

        favoriteExistenceResponse = existFavorite(loginToken, stationYeoksam.getId(), stationHanti.getId());
        // TODO: 2020/05/26 추후 false로 변경 해야함
        assertThat(favoriteExistenceResponse.isExistence()).isTrue();
    }

    private FavoriteExistenceResponse existFavorite(TokenResponse loginToken, Long sourceStationId,
        Long targetStationId) {
        return
            given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                auth().
                oauth2(loginToken.getAccessToken()).
                when().
                get("/me/favorites/source/{sourceId}/target/{targetId}/existsPath", sourceStationId, targetStationId).
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(FavoriteExistenceResponse.class);
    }

    private void addFavorite(TokenResponse loginToken, Long sourceStationId, Long targetStationId) {
        FavoriteRequest request = new FavoriteRequest(sourceStationId, targetStationId);
        given().
            accept(MediaType.APPLICATION_JSON_VALUE).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            auth().
            oauth2(loginToken.getAccessToken()).
            body(request).
            when().
            post("/me/favorites").
            then().
            log().all().
            statusCode(HttpStatus.CREATED.value());
    }

    private void removeFavorite(TokenResponse loginToken, Long sourceStationId, Long targetStationId) {
        given().
            accept(MediaType.APPLICATION_JSON_VALUE).
            auth().
            oauth2(loginToken.getAccessToken()).
            when().
            delete("/me/favorites/source/{sourceId}/target/{targetId}", sourceStationId, targetStationId).
            then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }

    private List<FavoriteResponse> findAllFavorites(TokenResponse loginToken) {
        return
            given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                auth().
                oauth2(loginToken.getAccessToken()).
                when().
                get("/me/favorites").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().
                jsonPath().getList(".", FavoriteResponse.class);
    }
}
