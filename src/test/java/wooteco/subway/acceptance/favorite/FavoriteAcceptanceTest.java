package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.acceptance.AcceptanceTest;
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
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse token = loginMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // when : 즐겨찾기 추가
        addMyFavorite(token, stationResponse1.getId(), stationResponse3.getId());
        // then : 즐겨찾기 조회
        List<FavoriteResponse> favorites = getAllMyFavorites(token);
        assertThat(favorites.get(0).getSourceStationId()).isEqualTo(stationResponse1.getId());
        assertThat(favorites.get(0).getTargetStationId()).isEqualTo(stationResponse3.getId());

        // when : 즐겨찾기 삭제
        removeFavorite(token, favorites.get(0).getId());
        favorites = getAllMyFavorites(token);
        assertThat(favorites.size()).isEqualTo(0);
    }

}
