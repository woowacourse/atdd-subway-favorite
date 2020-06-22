package wooteco.subway.domain.favoritepath;

import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FavoritePathTest {

    @Test
    void match() {
        Station start = mock(Station.class);
        Station end = mock(Station.class);
        when(start.getId()).thenReturn(1L);
        when(end.getId()).thenReturn(2L);
        FavoritePath favoritePath = new FavoritePath(start, end);

        assertThat(favoritePath.match(start, end)).isTrue();
    }

    @Test
    void notMatch() {
        Station start = mock(Station.class);
        Station end = mock(Station.class);
        Station weird = mock(Station.class);
        when(start.getId()).thenReturn(1L);
        when(end.getId()).thenReturn(2L);
        when(weird.getId()).thenReturn(3L);
        FavoritePath favoritePath = new FavoritePath(start, weird);

        assertThat(favoritePath.match(start, end)).isFalse();
    }
}