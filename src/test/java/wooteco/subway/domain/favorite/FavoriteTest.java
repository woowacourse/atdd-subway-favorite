package wooteco.subway.domain.favorite;

import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {

    @Test
    void isSameSourceAndTarget() {
        Station source = new Station(1L, "홍대역");
        Station target = new Station(2L, "합정역");

        Favorite favorite = new Favorite(1L, source.getId(), target.getId());
        assertThat(favorite.isSameSourceAndTarget(source, target)).isTrue();
    }

    @Test
    void isNotSameSourceAndTarget() {
        Station source = new Station(1L, "홍대역");
        Station target = new Station(2L, "합정역");

        Favorite favorite = new Favorite(1L, 10L, 20L);
        assertThat(favorite.isSameSourceAndTarget(source, target)).isFalse();
    }

    @Test
    void isSameId() {
        Long favoriteId = 1L;
        Favorite favorite = new Favorite(favoriteId, 1L, 2L);
        assertThat(favorite.isSameId(favoriteId)).isTrue();
    }

    @Test
    void isNotSameId() {
        Favorite favorite = new Favorite(2L, 1L, 2L);
        Long anotherId = 1L;
        assertThat(favorite.isSameId(anotherId)).isFalse();
    }
}