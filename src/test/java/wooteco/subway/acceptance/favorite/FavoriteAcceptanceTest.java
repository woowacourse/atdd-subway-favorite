package wooteco.subway.acceptance.favorite;

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

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 기능 관리")
    @Test
    void manageFavorite() {
        //Given 지하철 노선, 역, 구간이 등록되어 있다.
        LineResponse lineResponse = createLine("2호선");
        StationResponse gangnam = createStation("강남역");
        StationResponse jamsil = createStation("잠실역");
        addLineStation(lineResponse.getId(), null, gangnam.getId());
        addLineStation(lineResponse.getId(), gangnam.getId(), jamsil.getId());

        //And 사용자는 로그인이 되어있다.
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();

        //When: 사용자는 즐겨찾기 추가를 요청한다.
        String favoriteId = addFavorite(accessToken, gangnam.getId(), jamsil.getId());

        //When: 사용자는 즐겨찾기 목록 조회를 요청한다.
        FavoriteListResponse favoriteListResponse = getFavorites(accessToken);

        //Then: 사용자의 즐겨찾기 목록에 해당 경로가 추가된다.
        assertThat(favoriteListResponse.getFavoriteResponses().size()).isEqualTo(1);

        //When: 사용자는 즐겨찾기 삭제를 요청한다.
        deleteFavorite(accessToken, favoriteId);

        //Then: 사용자의 즐겨찾기 목록에 해당 경로가 삭제된다.
        FavoriteListResponse favoriteListResponseAfterDelete = getFavorites(accessToken);
        assertThat(favoriteListResponseAfterDelete.getFavoriteResponses().size()).isEqualTo(0);
    }

    private void deleteFavorite(String token, String favoriteId) {
        given()
                .auth()
                .oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/members/favorites/" + favoriteId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
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

    private String addFavorite(String token, Long departureId, Long destinationId) {
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(departureId,
                destinationId);

        String location = given().
                auth().
                oauth2(token).
                body(favoriteCreateRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members/favorites").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).extract().header("Location");
        return location.split("/")[3];
    }
}

