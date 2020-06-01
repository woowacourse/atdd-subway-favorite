package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

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

	@DisplayName("즐겨찾기 추가")
	@Test
	void add() {
		favorites.add(favorite2);

		assertThat(favorites.getFavorites().size()).isEqualTo(2);
	}

	@DisplayName("즐겨찾기 삭제")
	@Test
	void remove() {
		favorites.remove(favorite);

		assertThat(favorites.getFavorites().size()).isEqualTo(0);
	}

	@DisplayName("즐겨찾기에 추가된 모든 역들의 id를 중복없이 반환")
	@Test
	void extractStationIds() {
		favorites.add(favorite2);
		Set<Long> ids = favorites.extractStationIds();
		assertThat(ids).contains(1L, 2L, 3L);
	}

	@DisplayName("출발역과 도착역 id에 해당되는 즐겨찾기가 있는지 확인")
	@Test
	void hasFavoriteOf() {
		assertThat(favorites.hasFavoriteOf(1L, 2L)).isTrue();
		assertThat(favorites.hasFavoriteOf(1L, 3L)).isFalse();
	}
}