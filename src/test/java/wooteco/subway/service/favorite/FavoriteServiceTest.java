package wooteco.subway.service.favorite;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteDetail;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;

    private Long memberId;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        this.favoriteService = new FavoriteService(favoriteRepository, stationRepository);
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void addFavorite() {
        favoriteService.addFavorite(memberId, new FavoriteRequest(2L, 3L));
        verify(favoriteRepository).save(any());
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    void getFavorites() {
        final List<Favorite> favorites = Arrays.asList(new Favorite(1L, memberId, 2L, 3L));
        given(favoriteRepository.findAllByMemberId(anyLong())).willReturn(favorites);
        Set<Long> stationIds = new HashSet<>(Arrays.asList(2L, 3L));

        List<Station> stations = Arrays.asList(new Station(2L, "잠실역"), new Station(3L, "삼성역"));
        given(stationRepository.findAllById(stationIds)).willReturn(stations);

        List<FavoriteDetail> favoritesDetails = favoriteService.getFavorites(memberId);

        verify(favoriteRepository).findAllByMemberId(anyLong());
        verify(stationRepository).findAllById(stationIds);
        assertThat(favoritesDetails.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void removeFavoriteById() {
        favoriteService.removeFavoriteById(memberId, 1L);
        verify(favoriteRepository).deleteByIdWithMemberId(memberId, 1L);
    }

    @Test
    @DisplayName("즐겨찾기 여부 확인")
    void hasFavorite() {
        assertThat(favoriteService.hasFavorite(memberId, 1L, 2L)).isFalse();
    }
}
