package wooteco.subway.domain.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.LinkedHashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoritesTest {
    private Favorites favorites;

    @BeforeEach
    void setUp() {
        favorites = new Favorites(new LinkedHashSet<>());
    }

    @Test
    void add() {
        Favorite favorite = new Favorite(1L, 1L, 2L);
        favorites.add(favorite);
        assertThat(favorites.getFavorites()).hasSize(1);
    }

    @Test
    void deleteFavorite() {
        Favorite favorite = new Favorite(1L, 1L, 2L);
        favorites.add(favorite);
        favorites.delete(favorite.getId());
        assertThat(favorites.getFavorites()).hasSize(0);
    }
}