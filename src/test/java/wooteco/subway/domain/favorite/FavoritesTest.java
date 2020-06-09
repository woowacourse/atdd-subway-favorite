package wooteco.subway.domain.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoritesTest {

    private Favorites favorites;

    @BeforeEach
    void setUp() {
        Favorite favorite1 = new Favorite(1L, 1L, 1L, 2L);
        Favorite favorite2 = new Favorite(2L, 1L, 2L, 3L);
        favorites = new Favorites(Arrays.asList(favorite1, favorite2));
    }

    @DisplayName("즐겨찾기들에 속해있는 모든 역의 id들을 추출 테스트")
    @Test
    void getAllSourceTargetStationIdsTest() {
        List<Long> stationIds = favorites.getAllSourceTargetStationIds();
        assertThat(stationIds).hasSize(4);
        assertThat(stationIds).containsExactly(1L, 2L, 2L, 3L);
    }

    @DisplayName("역 이름 Map을 이용하여 응답용 즐겨찾기 목록 생성 테스트")
    @Test
    void toFavoriteResponsesTest() {
        Map<Long, String> stationNames = new HashMap<>();
        stationNames.put(1L, "가");
        stationNames.put(2L, "나");
        stationNames.put(3L, "다");

        List<FavoriteResponse> favoriteResponses = favorites.toFavoriteResponses(stationNames);
        assertThat(favoriteResponses).hasSize(2);
        assertThat(favoriteResponses.get(0).getId()).isEqualTo(1L);
        assertThat(favoriteResponses.get(1).getId()).isEqualTo(2L);
    }
}
