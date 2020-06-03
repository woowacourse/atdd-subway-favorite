package wooteco.subway.domain.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.exception.SameSourceTargetStationException;

class FavoriteTest {
    private static final Long FIRST_STATION_ID = 1L;
    private static final Long SECOND_STATION_ID = 2L;

    @DisplayName("출발역과 도착역이 다른 즐겨찾기 경로 인스턴스가 정상적으로 생성된다.")
    @Test
    void name1() {
        assertThatCode(() -> new Favorite(FIRST_STATION_ID, SECOND_STATION_ID))
            .doesNotThrowAnyException();
    }

    @DisplayName("출발역과 도착역이 같은 즐겨찾기 경로 인스턴스 생성시 SameSourceTargetStationException 예외를 발생시킨다.")
    @Test
    void name() {
        assertThatThrownBy(() -> new Favorite(FIRST_STATION_ID, FIRST_STATION_ID))
            .isInstanceOf(SameSourceTargetStationException.class);
    }
}