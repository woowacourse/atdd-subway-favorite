package wooteco.subway.domain.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @DisplayName("예외테스트: 출발역 혹은 도착역이 null인 경우")
    @Test
    void newFavorite_withNullParameters() {
        Long nullValue = null;
        Long longValue = 1L;

        assertThatThrownBy(() -> new Favorite(nullValue, nullValue))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("출발역이 null일 수 없습니다.");

        assertThatThrownBy(() -> new Favorite(nullValue, longValue))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("출발역이 null일 수 없습니다.");

        assertThatThrownBy(() -> new Favorite(longValue, nullValue))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("도착역이 null일 수 없습니다.");
    }

    @DisplayName("예외테스트: 출발역과 도착역이 같은 경우")
    @Test
    void newFavorite_whenDepartureAndArrivalIsSame() {
        //given
        Long departureStationId = 1L;

        //when, then
        assertThatThrownBy(() -> new Favorite(departureStationId, departureStationId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("도착역과 출발역은 같을 수 없습니다. station - " + departureStationId);
    }
}
