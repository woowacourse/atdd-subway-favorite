package wooteco.subway.domain.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @DisplayName("예외테스트: 출발역 혹은 도착역이 null인 경우")
    @Test
    void name() {
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
}
