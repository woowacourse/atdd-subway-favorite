package wooteco.subway.domain.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoritePathTest {
	private FavoritePath favoritePath;

	@BeforeEach
	void setUp() {
		this.favoritePath = FavoritePath.of(1L, 2L);
	}

	@DisplayName("똑같은 도착역과 출발역을 가진 즐겨찾기 경로와 역 id끼리 비교했을 때 true를 반환하는지 확인")
	@Test
	void ifCompareToSameFavoritePathThenReturnTrue() {
		FavoritePath samePath = FavoritePath.of(1L, 2L);

		assertThat(this.favoritePath.hasEqualStationIds(samePath)).isTrue();
	}

	@DisplayName("다른 도착역과 출발역을 가진 즐겨찾기 경로와 역 id끼리 비교했을 때 false를 반환하는지 확인")
	@Test
	void ifCompareToDifferentFavoritePathThenReturnFalse() {
		FavoritePath differentPath = FavoritePath.of(2L, 3L);

		assertThat(this.favoritePath.hasEqualStationIds(differentPath)).isFalse();
	}

	@DisplayName("도착역과 출발역 id를 잘 반환하는지 확인")
	@Test
	void getStationsId() {
		List<Long> stationsId = this.favoritePath.getStationsId();

		assertThat(stationsId.size()).isEqualTo(2);
		assertThat(stationsId.get(0)).isEqualTo(1L);
		assertThat(stationsId.get(1)).isEqualTo(2L);
	}
}
