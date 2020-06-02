package wooteco.subway.domain.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {
    private Long memberId;
    private Station departure;
    private Station arrival;

    @BeforeEach
    private void setUp() {
        memberId = 1L;
        departure = new Station(1L, "강남");
        arrival = new Station(2L, "도곡");
    }

    @Test
    void isDuplicate() {
        Favorite favorite = new Favorite(memberId, departure.getId(), arrival.getId());
        Favorite duplicated = new Favorite(memberId, departure.getId(), arrival.getId());

        assertThat(favorite.isDuplicate(duplicated)).isTrue();
    }

    @Test
    void isDuplicateWhenFail() {
        Favorite favorite = new Favorite(memberId, departure.getId(), arrival.getId());
        Station nonDuplicateDeparture = new Station(3L, "잠실");
        Station nonDuplicateArrival = new Station(4L, "석촌");
        Favorite duplicated = new Favorite(2L, nonDuplicateDeparture.getId(), nonDuplicateArrival.getId());

        assertThat(favorite.isDuplicate(duplicated)).isFalse();
    }
}