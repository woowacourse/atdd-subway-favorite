package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static wooteco.subway.AuthAcceptanceTest.login;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        initStation();
    }

    /**
     * Given 지하철 역이 여러개 추가 되어 있다.
     * And 지하철 노선이 추가 되어 있다.
     * And 지하철 구간이 추가 되어 있다.
     * And 로그인이 되어 있다.
     * <p>
     * When 최단 거리 요청을 보낸다.
     * And 즐겨 찾기 추가 요청을 보낸다.
     * <p>
     * Then 즐겨 찾기 목록을 응답 받는다.
     */

    @DisplayName("")
    @Test
    void favoriteTest() {
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        List<StationResponse> stations = findPath(STATION_NAME_KANGNAM, STATION_NAME_DOGOK, "DISTANCE").getStations();
        StationResponse startStation = stations.get(0);
        StationResponse endStation = stations.get(stations.size() - 1);

        createFavorite(tokenResponse, startStation.getId(), endStation.getId());
        findFavoriteById(tokenResponse);
    }
}
