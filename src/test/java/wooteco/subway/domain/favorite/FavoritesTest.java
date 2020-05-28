package wooteco.subway.domain.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.exception.EntityNotFoundException;

class FavoritesTest {
    private static final Long FIRST_STATION_ID = 1L;
    private static final Long SECOND_STATION_ID = 2L;
    private static final Long THIRD_STATION_ID = 3L;
    private static final Long FOURTH_STATION_ID = 4L;

    private static final Favorite NEW_FAVORITE = new Favorite(FIRST_STATION_ID, FOURTH_STATION_ID);
    private static final Favorite FIRST_FAVORITE = new Favorite(FIRST_STATION_ID, SECOND_STATION_ID);
    private static final Favorite SECOND_FAVORITE = new Favorite(FIRST_STATION_ID, THIRD_STATION_ID);
    private static final Favorite THIRD_FAVORITE = new Favorite(SECOND_STATION_ID, THIRD_STATION_ID);
    private static final Favorite FOURTH_FAVORITE = new Favorite(FOURTH_STATION_ID, FIRST_STATION_ID);

    private Favorites favorites;

    @BeforeEach
    void setUp() {
        Set<Favorite> favoriteSet = new HashSet<>(Arrays.asList(
            FIRST_FAVORITE, SECOND_FAVORITE, THIRD_FAVORITE, FOURTH_FAVORITE
        ));
        favorites = new Favorites(favoriteSet);
    }

    @DisplayName("즐겨 찾기 목록에 포함되어 있는 모든 역의 ID들을 반환")
    @Test
    void findAllIds() {
        Set<Long> actual = favorites.findAllIds();
        HashSet<Long> expected = new HashSet<>(Arrays.asList(FIRST_STATION_ID, SECOND_STATION_ID,
            THIRD_STATION_ID, FOURTH_STATION_ID));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("즐겨 찾기 목록에 포함되어 있는 즐겨찾기 경로 삭제시 해당 즐겨찾기 경로가 삭제된다.")
    @Test
    void removeFavorite() {
        favorites.removeFavorite(FIRST_FAVORITE);
        assertThat(favorites.getFavorites()).doesNotContain(FIRST_FAVORITE);
    }

    @DisplayName("즐겨 찾기 목록에 포함되어 있지 않는 즐겨찾기 경로 삭제시, EntityNotFoundException 예외를 던진다.")
    @Test
    void removeFavoriteWithNonExistingPath() {
        assertThatThrownBy(() -> favorites.removeFavorite(NEW_FAVORITE))
            .isInstanceOf(EntityNotFoundException.class);
    }
}