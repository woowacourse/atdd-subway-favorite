package wooteco.subway.domain.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoritesTest {

	@DisplayName("즐겨 찾기 목록에 포함되어 있는 모든 역의 ID들을 반환")
	@Test
	void findAllIds() {
		Set<Favorite> favorites = new HashSet<>(Arrays.asList(
			Favorite.of(1L, 2L).withId(1L),
			Favorite.of(1L, 3L).withId(2L),
			Favorite.of(2L, 3L).withId(3L),
			Favorite.of(4L, 1L).withId(4L)
		));
		Set<Long> actual = new Favorites(favorites).findAllIds();
		HashSet<Long> expected = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L));

		assertThat(actual).isEqualTo(expected);
	}
}