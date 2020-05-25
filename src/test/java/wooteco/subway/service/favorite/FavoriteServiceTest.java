package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

public class FavoriteServiceTest {
    @Mock
    FavoriteRepository favoriteRepository;

    FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        favoriteService = new FavoriteService(favoriteRepository);
    }

    @DisplayName("즐겨찾기 생성 테스트")
    @Test
    void createFavoriteTest() {
        Favorite favorite = new Favorite(10L, 2L, 3L);
        when(favoriteRepository.save(any())).thenReturn(favorite);

        FavoriteResponse favoriteResponse = favoriteService.createFavorite(10L,
            new FavoriteRequest(2L, 3L));

        verify(favoriteRepository).save(any());
        assertThat(favoriteResponse.getMemberId()).isEqualTo(10L);
        assertThat(favoriteResponse.getSourceStationId()).isEqualTo(2L);
        assertThat(favoriteResponse.getTargetStationId()).isEqualTo(3L);
    }

    @DisplayName("즐겨찾기 조회 테스트")
    @Test
    void getFavoritesTest() {
        Favorite favorite = new Favorite(5L, 2L, 3L);
        List<Favorite> favorites = Arrays.asList(favorite);
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(favorites);

        List<FavoriteResponse> responses = favoriteService.getFavorites(5L);
        assertThat(responses.get(0).getMemberId()).isEqualTo(5L);
        assertThat(responses.get(0).getSourceStationId()).isEqualTo(2L);
        assertThat(responses.get(0).getTargetStationId()).isEqualTo(3L);
    }

    @DisplayName("즐겨찾기 삭제 테스트")
    @Test
    void deleteFavoriteTest() {
        favoriteService.deleteFavorite(100L);
        verify(favoriteRepository).deleteById(eq(100L));
    }
}
