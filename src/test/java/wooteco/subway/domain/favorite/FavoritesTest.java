package wooteco.subway.domain.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoritesTest {
	Favorites favorites;

	@BeforeEach
	void setUp() {
		favorites = Favorites.empty();
	}

	@DisplayName("요소를 하나 추가한다.")
	@Test
	void add() {
		Favorites result = favorites.add(Favorite.of(1L, 2L));
		assertThat(result.getFavorites()).containsExactly(Favorite.of(1L, 2L));
	}

	@DisplayName("중복된 요소를 추가하면 예외를 발생시킨다.")
	@Test
	void add_DuplicatedFavorite() {
		Favorites given = favorites.add(Favorite.of(1L, 2L));

		assertThatThrownBy(() -> given.add(Favorite.of(1L, 2L)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요소를 하나 제거한다.")
	@Test
	void remove() {
		Favorites given = favorites.add(Favorite.of(1L, 2L));
		Favorites result = given.remove(1L, 2L);

		assertThat(result.getFavorites()).isEmpty();
	}

	@DisplayName("없는 요소를 제거하려 시도한다.")
	@Test
	void remove_NotExistFavorite() {
		assertThatThrownBy(() -> favorites.remove(1L, 2L))
			.isInstanceOf(IllegalArgumentException.class);
	}
}