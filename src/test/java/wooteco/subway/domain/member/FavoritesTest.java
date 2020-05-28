package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class FavoritesTest {
    @Test
    void addFavorite() {
        Favorites favorites = new Favorites(new ArrayList<>());
        favorites.addFavorite(new Favorite(1L, 2L));

        assertThat(favorites.size()).isEqualTo(1);
    }

    @Test
    void addFavorite_SameFavorite_ExceptionThrown() {
        Favorites favorites = new Favorites(new ArrayList<>());
        favorites.addFavorite(new Favorite(1L, 2L));

        assertThatThrownBy(() -> favorites.addFavorite(new Favorite(1L, 2L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중복되는");
    }
}
