package wooteco.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

@Disabled
@Sql("/truncate.sql")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("즐겨찾기 기능")
    @Test
    void favorite() {

        // given : 역들이 추가되어있다.
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        // and : 노선들이 추가되어있다.
        LineResponse lineResponse = createLine("2호선");

        // and : 노선에 역이 추가되어있다.
        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        // and : 회원이 등록되어있다.
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        // and : 로그인이 되어있다.
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // when : 즐겨찾기를 추가한다.
        createFavorite(tokenResponse, stationResponse1.getId(), stationResponse2.getId());

        // then : 즐겨찾기가 추가되었다.
        FavoriteResponse favoriteResponse = getFavorites(tokenResponse).get(0);
        assertThat(favoriteResponse.getPreStation().getName()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(favoriteResponse.getStation().getName()).isEqualTo(STATION_NAME_SEOLLEUNG);

        // when : 즐겨찾기 목록을 조회한다.
        List<FavoriteResponse> favoritesRespons = getFavorites(tokenResponse);

        // then : 즐겨찾기 목록이 n개다.
        assertThat(favoritesRespons.size()).isEqualTo(1);

        // when : 즐겨찾기를 삭제한다.
        deleteFavorite(tokenResponse);

        // then : 즐겨찾기가 삭제되었다.
        assertThat(favoritesRespons.size()).isEqualTo(0);
    }
}
