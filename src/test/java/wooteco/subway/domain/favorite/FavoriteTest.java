package wooteco.subway.domain.favorite;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {

    @Test
    void isDuplicate() {
        Long memberId = 1L;
        String departure = "강남";
        String arrival = "도곡";
        Favorite favorite = new Favorite(memberId, departure, arrival);
        Favorite duplicated = new Favorite(memberId, departure, arrival);

        assertThat(favorite.isDuplicate(duplicated)).isTrue();
    }

    @Test
    void isDuplicateWhenFail() {
        Long memberId = 1L;
        String departure = "강남";
        String arrival = "도곡";
        Favorite favorite = new Favorite(memberId, departure, arrival);
        Favorite duplicated = new Favorite(2L, "잠실", "석촌");

        assertThat(favorite.isDuplicate(duplicated)).isFalse();
    }
}