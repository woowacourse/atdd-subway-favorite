package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("즐겨찾기 관리")
    void manageFavorites() {
        // given
        LineResponse lineResponse = createLine("8호선");
        StationResponse stationResponse = createStation("잠실역");
        StationResponse stationResponse1 = createStation("석촌역");
        addLineStation(lineResponse.getId(), null, stationResponse.getId(), 0, 0);
        addLineStation(lineResponse.getId(), stationResponse.getId(), stationResponse1.getId(), 10,
            10);

        // 회원 가입
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        // 1. 로그인
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // 2. 즐겨찾기 추가
        addFavorite(tokenResponse, stationResponse.getId(), stationResponse1.getId());

        // 3. 즐겨찾기 조회
        List<FavoriteResponse> favoriteResponse = getFavorite(tokenResponse);
        assertThat(favoriteResponse.size()).isEqualTo(1);
        assertThat(favoriteResponse.get(0).getSourceId()).isEqualTo(stationResponse.getId());
        assertThat(favoriteResponse.get(0).getTargetId()).isEqualTo(stationResponse1.getId());
        assertThat(favoriteResponse.get(0).getSourceName()).isEqualTo(stationResponse.getName());
        assertThat(favoriteResponse.get(0).getTargetName()).isEqualTo(stationResponse1.getName());

        // 4. 즐겨찾기 제거
        removeFavorite(tokenResponse,
            new FavoriteRequest(stationResponse.getId(), stationResponse1.getId()));
        favoriteResponse = getFavorite(tokenResponse);
        assertThat(favoriteResponse.size()).isEqualTo(0);
    }

    private void addFavorite(TokenResponse tokenResponse, Long sourceId, Long targetId) {
        Map<String, Object> params = new HashMap<>();
        params.put("sourceStationId", sourceId);
        params.put("targetStationId", targetId);

        //@formatter:off
        given()
            .auth()
            .oauth2(tokenResponse.getAccessToken())
            .body(params)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .post("/me/favorites")
        .then()
            .log().all()
            .statusCode(HttpStatus.OK.value());
        //@formatter:on
    }

    private List<FavoriteResponse> getFavorite(TokenResponse tokenResponse) {
        //@formatter:off
        return
        given()
            .auth()
            .oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .get("/me/favorites")
        .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getList(".", FavoriteResponse.class);
        //@formatter:on
    }

    private void removeFavorite(TokenResponse tokenResponse, FavoriteRequest favoriteRequest) {
        //@formatter:off
        given()
            .auth()
            .oauth2(tokenResponse.getAccessToken())
            .body(favoriteRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .delete("/me/favorites")
        .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }
}
