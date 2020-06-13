package wooteco.subway.domain.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    void isDuplicate() {
        Long memberId = 1L;
        Long departure = 1L;
        Long arrival = 2L;
        Favorite favorite = new Favorite(memberId, departure, arrival);
        Favorite duplicated = new Favorite(memberId, departure, arrival);

        assertThat(favorite.isDuplicate(duplicated)).isTrue();
    }

    @Test
    void isDuplicateWhenFail() {
        Long memberId = 1L;
        Long departure = 1L;
        Long arrival = 2L;
        Favorite favorite = new Favorite(memberId, departure, arrival);
        Favorite duplicated = new Favorite(2L, 3L, 4L);

        assertThat(favorite.isDuplicate(duplicated)).isFalse();
    }
}