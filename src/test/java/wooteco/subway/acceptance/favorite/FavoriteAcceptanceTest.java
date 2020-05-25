package wooteco.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteListResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

/*
 * Feature: 즐겨찾기 기능 테스트
 *
 * Scenario: 즐겨찾기 추가
 * Given 지하철 노선, 역, 구간이 등록되어 있다.
 * And 사용자는 로그인이 되어있다.
 * And 사용자는 경로를 조회한다.
 * When: 즐겨찾기 추가 버튼을 통해 요청한다.
 * Then: 사용자의 즐겨찾기 목록에 해당 경로가 추가된다.
 *
 * Scenario: 즐겨찾기 조회
 * Given 지하철 노선, 역, 구간이 등록되어 있다.
 * And 사용자는 로그인이 되어있다.
 * When: 즐겨찾기 조회 버튼을 통해 목록을 요청한다.
 * Then: 사용자의 즐겨찾기 목록을 보여준다.
 *
 * Scenario: 즐겨찾기 삭제
 * Given 지하철 노선, 역, 구간이 등록되어 있다.
 * And 사용자는 로그인이 되어있다.
 * When: 즐겨찾기 삭제 버튼을 통해 삭제를 요청한다.
 * Then: 사용자의 즐겨찾기 목록에 해당 경로가 삭제된다.
 *
 * */

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 기능 관리")
    @Test
    void manageFavorite() {
        LineResponse lineResponse = createLine("2호선");
        StationResponse gangnam = createStation("강남역");
        StationResponse jamsil = createStation("잠실역");
        addLineStation(lineResponse.getId(), null, gangnam.getId());
        addLineStation(lineResponse.getId(), gangnam.getId(), jamsil.getId());

        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        addFavorite(tokenResponse.getAccessToken(), gangnam.getId(), jamsil.getId());

        FavoriteListResponse favoriteListResponse = getFavorites(tokenResponse.getAccessToken());
        assertThat(favoriteListResponse.getFavoriteResponses().size()).isEqualTo(1);

    }

    private FavoriteListResponse getFavorites(String token) {
        return given()
            .auth()
            .oauth2(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/members/favorites")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().as(FavoriteListResponse.class);
    }

    private void addFavorite(String token, Long departureId, Long destinationId) {
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(departureId,
            destinationId);

        given().
            auth().
            oauth2(token).
            body(favoriteCreateRequest).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            post("/members/favorites").
            then().
            log().all().
            statusCode(HttpStatus.CREATED.value());
    }
}

