package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FavoriteServiceTest {

    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
    }

    @Test
    public void addFavorite() {
        final Favorite favorite = new Favorite(10L, 63L, 1L, 3L);
        when(favoriteRepository.save(any())).thenReturn(favorite);

        FavoriteResponse response = favoriteService.addFavorite(63L, new FavoriteRequest(1L, 3L));

        verify(favoriteRepository).save(any());
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getMemberId()).isEqualTo(63L);
        assertThat(response.getSourceStationId()).isEqualTo(1L);
        assertThat(response.getTargetStationId()).isEqualTo(3L);
    }

    @Test
    public void showMyAllFavorites() {
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(new Favorite(10L, 63L, 1L, 3L));
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(favorites);

        List<FavoriteResponse> responses = favoriteService.showMyAllFavorites(63L);

        verify(favoriteRepository).findAllByMemberId(eq(63L));
        assertThat(responses.get(0).getId()).isEqualTo(10L);
        assertThat(responses.get(0).getMemberId()).isEqualTo(63L);
        assertThat(responses.get(0).getSourceStationId()).isEqualTo(1L);
        assertThat(responses.get(0).getTargetStationId()).isEqualTo(3L);
    }

    @Test
    public void removeMyFavorite() {
        favoriteService.removeFavorite(10L);
        verify(favoriteRepository).deleteById(eq(10L));
    }

}