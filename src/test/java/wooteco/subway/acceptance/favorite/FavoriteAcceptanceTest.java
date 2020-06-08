package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("즐겨찾기를 관리(추가, 조회, 삭제) 한다.")
    @Test
    public void manageFavorites() {
        // given(line, station, lineStation, member, login)
        createLine(LINE_NAME_2);
        List<LineResponse> lines = getLines();

        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        List<StationResponse> stations = getStations();

        addLineStation(lines.get(0).getId(), null, stations.get(0).getId());
        addLineStation(lines.get(0).getId(), stations.get(0).getId(), stations.get(1).getId());
        addLineStation(lines.get(0).getId(), stations.get(1).getId(), stations.get(2).getId());

        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // when : 즐겨찾기 추가
        addMyFavorite(token, stations.get(0).getId(), stations.get(2).getId());
        // then : 즐겨찾기 조회
        List<FavoriteResponse> favorites = getAllMyFavorites(token);
        assertThat(favorites.get(0).getSourceStationId()).isEqualTo(stations.get(0).getId());
        assertThat(favorites.get(0).getTargetStationId()).isEqualTo(stations.get(2).getId());

        // when : 즐겨찾기 삭제
        removeFavorite(token, favorites.get(0).getId());
        // then : 즐겨찾기 조회
        favorites = getAllMyFavorites(token);
        assertThat(favorites.size()).isEqualTo(0);
    }

    private void addMyFavorite(final TokenResponse token, final Long source, final Long target) {
        FavoriteRequest request = new FavoriteRequest(source, target);
        // @formatter:off
        given().
                header("Authorization", token.getTokenType() + " " + token.getAccessToken()).
                body(request).
                accept(MediaType.APPLICATION_JSON_VALUE).
                contentType(MediaType.APPLICATION_JSON_VALUE).
        when().
                post("/favorites").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        // @formatter:on
    }

    private List<FavoriteResponse> getAllMyFavorites(final TokenResponse token) {
        // @formatter:off
        return given().
                header("Authorization", token.getTokenType() + " " + token.getAccessToken()).
        when().
                get("/favorites").
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().
                jsonPath().getList(".", FavoriteResponse.class);
        // @formatter:on
    }

    private void removeFavorite(final TokenResponse token, final Long id) {
        // @formatter:off
        given().
                header("Authorization", token.getTokenType() + " " + token.getAccessToken()).
        when().
                delete("/favorites/" + id).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter:on
    }

}
