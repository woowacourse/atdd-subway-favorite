package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.FavoriteResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("즐겨찾기 목록을 관리한다")
    @Test
    void manageFavorite() {
        //when
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        createStation(STATION_NAME_YANGJAE);
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        createFavorite(STATION_NAME_YEOKSAM, STATION_NAME_SEOLLEUNG);
        createFavorite(STATION_NAME_SEOLLEUNG, STATION_NAME_YANGJAE);

        //when
        List<FavoriteResponse> favoriteResponses = getFavorites();
        //then
        assertThat(favoriteResponses.size()).isEqualTo(2);
        assertThat(favoriteResponses.get(0).getSourceName()).isEqualTo(STATION_NAME_YEOKSAM);
        assertThat(favoriteResponses.get(0).getDestinationName()).isEqualTo(STATION_NAME_SEOLLEUNG);
        assertThat(favoriteResponses.get(1).getSourceName()).isEqualTo(STATION_NAME_SEOLLEUNG);
        assertThat(favoriteResponses.get(1).getDestinationName()).isEqualTo(STATION_NAME_YANGJAE);

        //when
        deleteFavorite(STATION_NAME_YEOKSAM, STATION_NAME_SEOLLEUNG);
        favoriteResponses = getFavorites();
        //then
        assertThat(favoriteResponses.size()).isEqualTo(1);
        assertThat(favoriteResponses.get(0).getSourceName()).isEqualTo(STATION_NAME_SEOLLEUNG);
        assertThat(favoriteResponses.get(0).getDestinationName()).isEqualTo(STATION_NAME_YANGJAE);
    }
}
