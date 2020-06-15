package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favorite = new Favorite(1L, 1L, 2L);
    }

    @Test
    void isSameId_True() {
        assertThat(favorite.isSameId(1L)).isTrue();
    }

    @Test
    void isSameId_False() {
        assertThat(favorite.isSameId(2L)).isFalse();
    }
}