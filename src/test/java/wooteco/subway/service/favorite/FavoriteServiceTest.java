package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Sql("/truncate.sql")
@DataJdbcTest
@Import(FavoriteService.class)
public class FavoriteServiceTest {
    private final Long MEMBER_ID = 1L;
    private final Long SOURCE = 2L;
    private final Long TARGET = 3L;

    @Autowired
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService.addFavorite(MEMBER_ID, new FavoriteRequest(SOURCE, TARGET));
    }

    @Test
    void addFavoriteTest() {
        long SECOND_SOURCE = 4L;
        long SECOND_TARGET = 5L;

        favoriteService.addFavorite(
            MEMBER_ID, new FavoriteRequest(SECOND_SOURCE, SECOND_TARGET));

        List<FavoriteResponse> favorites = favoriteService.getFavorites(MEMBER_ID);
        assertThat(favorites.size()).isEqualTo(2);
    }

    @Test
    void getFavoriteTest() {
        FavoriteResponse favoriteResponse = favoriteService.getFavorite(MEMBER_ID, SOURCE, TARGET);
        assertThat(favoriteResponse.getId()).isEqualTo(MEMBER_ID);
    }

    @Test
    void getFavoritesTest() {
        List<FavoriteResponse> favoriteResponses = favoriteService.getFavorites(MEMBER_ID);
        assertThat(favoriteResponses).size().isEqualTo(1);
    }

    @Test
    void removeFavoriteTest() {
        favoriteService.removeFavorite(MEMBER_ID, SOURCE, TARGET);
        List<FavoriteResponse> favoriteResponses = favoriteService.getFavorites(MEMBER_ID);
        assertThat(favoriteResponses).size().isEqualTo(0);
    }
}
