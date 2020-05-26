package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() throws Exception {
        // given
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");
        StationResponse station3 = createStation("station3");

        LineResponse line1 = createLine("line1");
        addLineStation(line1.getId(), null, station1.getId());
        addLineStation(line1.getId(), station1.getId(), station2.getId());
        addLineStation(line1.getId(), station2.getId(), station3.getId());

        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        createMember(TEST_USER_EMAIL2, TEST_USER_NAME2, TEST_USER_PASSWORD2);

        // when
        FavoriteRequest request = new FavoriteRequest(station1.getId(), station3.getId());
        TokenResponse token1 = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        createFavorite(request, token1);

        List<FavoriteResponse> favorites = getAllFavorites(token1);
        Long createdId = favorites.get(0).getId();

        // then
        assertThat(favorites).isNotNull();
        assertThat(createdId).isNotNull();
        assertThat(favorites.get(0).getSourceStationId()).isEqualTo(station1.getId());
        assertThat(favorites.get(0).getSourceStationName()).isEqualTo(station1.getName());
        assertThat(favorites.get(0).getTargetStationId()).isEqualTo(station3.getId());
        assertThat(favorites.get(0).getTargetStationName()).isEqualTo(station3.getName());

        // when
        deleteFavorite(createdId, token1);

        // then
        List<FavoriteResponse> favoritesAfterDelete = getAllFavorites(token1);
        assertThat(favoritesAfterDelete).isEmpty();
    }
}
