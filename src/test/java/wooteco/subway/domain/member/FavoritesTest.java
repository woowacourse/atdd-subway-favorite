package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoritesTest {

    private Favorite favorite;
    private Favorite favorite2;
    private Favorites favorites;

    @BeforeEach
    void setUp() {
        favorite = new Favorite(1L, 2L);
        favorite2 = new Favorite(2L, 3L);
        favorites = Favorites.empty();
        favorites.add(favorite);
    }

    @DisplayName("즐겨찾기 추가 기능")
    @Test
    void add() {
        favorites.add(favorite2);

        assertThat(favorites.getFavorites().size()).isEqualTo(2);
    }

    @DisplayName("즐겨찾기 삭제 기능")
    @Test
    void remove() {
        favorites.remove(favorite);

        assertThat(favorites.getFavorites().size()).isEqualTo(0);
    }
}