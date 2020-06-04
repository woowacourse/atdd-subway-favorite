package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    private static final long SAMSEOK = 1L;
    private static final long DONGSAN = 2L;
    private static final long JAMSIL = 3L;
    private static final long SEOKCHONGOBUN = 4L;
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    public void setUp() {
        favoriteService = new FavoriteService(stationRepository, favoriteRepository);
    }

    @Test
    public void createFavoriteTest() {
        FavoriteRequest favoriteRequest = new FavoriteRequest("삼척", "동산");
        Favorite favorite = Favorite.of(1L, SAMSEOK, DONGSAN);

        when(favoriteRepository.save(any())).thenReturn(favorite);
        Favorite persistFavorite = favoriteService.createFavorite(1L, favoriteRequest);

        assertThat(persistFavorite).isEqualTo(favorite);
    }

    @Test
    public void getFavoritesTest() {
        Favorite favorite = Favorite.of(1L, JAMSIL, SEOKCHONGOBUN);
        Station sourceStation = new Station(1L, "잠실");
        Station targetStation = new Station(2L, "석촌고분");

        when(favoriteRepository.findByMemberId(any())).thenReturn(Arrays.asList(favorite));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(targetStation));

        List<FavoriteResponse> favorites = favoriteService.getFavoriteResponse(1L);

        assertThat(favorites).hasSize(1);
        assertThat(favorites.get(0).getSource()).isEqualTo("잠실");
        assertThat(favorites.get(0).getTarget()).isEqualTo("석촌고분");
    }
}