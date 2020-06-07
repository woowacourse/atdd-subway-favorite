package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponses;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;

    private FavoriteService favoriteService;

    private Favorite favorite1;
    private Favorite favorite2;
    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        favorite1 = new Favorite(1L, 1L, 1L, 2L);
        favorite2 = new Favorite(2L, 1L, 2L, 3L);
        station1 = new Station(1L, STATION_NAME_KANGNAM);
        station2 = new Station(2L, STATION_NAME_YEOKSAM);
        station3 = new Station(3L, STATION_NAME_SEOLLEUNG);
    }

    @DisplayName("본인의 모든 즐겨찾기 목록을 잘 가져오는지 테스트")
    @Test
    void findAllFavoriteResponsesTest() {
        given(favoriteRepository.findAllByMemberId(any())).willReturn(Arrays.asList(favorite1, favorite2));
        given(stationRepository.findAllById(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(station1, station2));
        given(stationRepository.findAllById(Arrays.asList(2L, 3L))).willReturn(Arrays.asList(station2, station3));

        FavoriteResponses favoriteResponses = favoriteService.findAllFavoriteResponses(1L);
        assertThat(favoriteResponses).isNotNull();
    }

    @DisplayName("본인의 즐겨찾기 목록에 새로운 즐겨찾기가 잘 추가되는지 테스트")
    @Test
    void createFavoriteTest() {

        given(stationRepository.findIdByName(STATION_NAME_KANGNAM)).willReturn(1L);
        given(stationRepository.findIdByName(STATION_NAME_YEOKSAM)).willReturn(2L);

        FavoriteCreateRequest request = new FavoriteCreateRequest(STATION_NAME_KANGNAM, STATION_NAME_YEOKSAM);
        favoriteService.createFavorite(1L, request);

        verify(favoriteRepository).save(any());
    }

    @DisplayName("본인의 즐겨찾기 목록에서 즐겨찾기 id로 잘 삭제되는지 테스트")
    @Test
    void deleteFavoriteTest() {
        favoriteService.deleteFavorite(1L);

        verify(favoriteRepository).deleteById(any());
    }
}
