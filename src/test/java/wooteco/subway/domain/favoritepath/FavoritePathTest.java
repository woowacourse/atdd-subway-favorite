package wooteco.subway.domain.favoritepath;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;

class FavoritePathTest {

    @Test
    void match() {
        Station start = new Station(1L, "신정역");
        Station end = new Station(2L, "잠실역");

        FavoritePath favoritePath = new FavoritePath(start, end);

        assertThat(favoritePath.match(start, end)).isTrue();
    }

    @Test
    void notMatch() {
        Station start = new Station(1L, "신정역");
        Station end = new Station(2L, "잠실역");
        Station weird = new Station(3L, "구미역");

        FavoritePath favoritePath = new FavoritePath(start, end);

        assertThat(favoritePath.match(start, weird)).isFalse();
    }
}
