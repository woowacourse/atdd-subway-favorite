package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
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
import wooteco.subway.domain.station.StationRepository;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    private final Long MEMBER_ID = 1L;
    private final Long SOURCE = 2L;
    private final Long TARGET = 3L;

    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
    }

    @Test
    void addFavoriteTest() {
        FavoriteRequest favoriteRequest = new FavoriteRequest();

        when(favoriteRepository.save(any(Favorite.class))).thenReturn(
            Favorite.of(MEMBER_ID, SOURCE, TARGET));
        favoriteService.addFavorite(MEMBER_ID, favoriteRequest);
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void hasFavoriteTest() {
        when(favoriteRepository.findBySourceAndTargetAndMember(anyLong(), anyLong(),
            anyLong())).thenReturn(
            Optional.of(new Favorite(1L, 2L, 3L, 4L)));

        FavoriteResponse favoriteResponse = favoriteService.getFavorite(MEMBER_ID, SOURCE, TARGET);
        assertThat(favoriteResponse).isNotNull();
        verify(favoriteRepository).findBySourceAndTargetAndMember(MEMBER_ID, SOURCE, TARGET);
    }

    @Test
    void getFavoritesTest() {
        final List<Favorite> favorites = Arrays.asList(Favorite.of(MEMBER_ID, SOURCE, TARGET),
            Favorite.of(MEMBER_ID, SOURCE, TARGET));
        when(favoriteRepository.findAllByMemberId(anyLong())).thenReturn(favorites);
        List<FavoriteResponse> favoriteResponses = favoriteService.getFavorites(MEMBER_ID);
        assertThat(favoriteResponses).size().isEqualTo(2);
    }

    @Test
    void removeFavoriteTest() {
        when(favoriteRepository.deleteByMemberIdAndSourceAndTarget(anyLong(), anyLong(), anyLong()))
            .thenReturn(true);
        favoriteService.removeFavorite(MEMBER_ID, 1L, 2L);
        verify(favoriteRepository).deleteByMemberIdAndSourceAndTarget(MEMBER_ID, 1L, 2L);
    }
}
