package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    public void setUp() {
        favoriteService = new FavoriteService(favoriteRepository);
    }

    @Test
    public void createFavoriteTest() {
        Favorite favorite = new Favorite(1L, "삼척", "동산");
        when(favoriteRepository.save(any())).thenReturn(favorite);
        Favorite persistFavorite = favoriteService.createFavorite(favorite);

        assertThat(persistFavorite).isEqualTo(favorite);
    }

    @Test
    public void getFavoritesTest() {
        Favorite favorite = new Favorite(1L, "잠실", "석촌고분");

        when(favoriteRepository.findByMemberId(any())).thenReturn(Arrays.asList(favorite));

        List<Favorite> favorites = favoriteService.getFavorites(1L);

        assertThat(favorites).hasSize(1);
        assertThat(favorites.get(0).getSource()).isEqualTo("잠실");
        assertThat(favorites.get(0).getTarget()).isEqualTo("석촌고분");
    }
}