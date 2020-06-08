package wooteco.subway.domain.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;
import wooteco.subway.exceptions.NotExistFavoritePathsException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoritePathsTest {
	private FavoritePaths favoritePaths;

	@BeforeEach
	void setUp() {
		this.favoritePaths = FavoritePaths.empty();
		this.favoritePaths.addPath(FavoritePath.of(1L, 2L));
	}

	@DisplayName("중복되지 않는 즐겨찾기 경로를 잘 추가하는지 확인")
	@Test
	void succeedToAddNotDuplicatedPath() {
		FavoritePath notDuplicatedPath = FavoritePath.of(3L, 4L);
		this.favoritePaths.addPath(notDuplicatedPath);

		List<Long> expectedStationsIds = Arrays.asList(1L, 2L, 3L, 4L);

		assertThat(this.favoritePaths.getStationsIds()).isEqualTo(expectedStationsIds);
	}

	@DisplayName("중복된 즐겨찾기 경로를 추가할 때 예외가 발생하는지 확인")
	@Test
	void failToAddDuplicatedPath() {
		FavoritePath duplicatedPath = FavoritePath.of(1L, 2L);

		assertThatThrownBy(() -> this.favoritePaths.addPath(duplicatedPath))
				.isInstanceOf(DuplicatedFavoritePathException.class)
				.hasMessage("이미 등록된 즐겨찾기 경로입니다!");
	}

	@DisplayName("등록된 즐겨찾기 경로가 있는 상태에서, 최근 등록된 경로를 찾을 시 경로가 잘 반환되는지 확인")
	@Test
	void getRecentlyUpdatedPathWhenFavoritePathsExist() {
		FavoritePath favoritePath = this.favoritePaths.getRecentlyUpdatedPath();

		assertThat(favoritePath.getSourceId()).isEqualTo(1L);
		assertThat(favoritePath.getTargetId()).isEqualTo(2L);
	}

	@DisplayName("등록된 즐겨찾기 경로가 없는 상태에서, 최근 등록된 경로를 찾을 시 예외가 발생하는지 확인")
	@Test
	void getRecentlyUpdatedPathWhenFavoritePathsNotExistThenThrowException() {
		FavoritePaths newFavoritePaths = FavoritePaths.empty();

		assertThatThrownBy(newFavoritePaths::getRecentlyUpdatedPath)
				.isInstanceOf(NotExistFavoritePathsException.class)
				.hasMessage("등록된 즐겨찾기 경로가 없습니다!");
	}
}
