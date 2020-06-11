package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FavoritesTest {

    private Favorites favorites;

    @BeforeEach
    void setUp() {
        favorites = Favorites.empty();
        favorites.add(new Favorite(1L, 1L, 2L));
        favorites.add(new Favorite(2L, 2L, 3L));
    }

    @Test
    void add() {
        Favorite favorite = new Favorite(3L, 3L, 4L);
        favorites.add(favorite);
        assertThat(favorites.getFavorites()).contains(favorite);
    }

    @Test
    void remove() {
        favorites.remove(1L);
        assertThat(favorites.getFavorites()).hasSize(1);
    }

    @Test
    void getFavoriteStationIds() {
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        assertThat(favorites.getFavoriteStationIds()).isEqualTo(ids);
    }
}