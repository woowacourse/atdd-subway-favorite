package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.exception.NotExistStationDataException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    StationRepository stationRepository;

    FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
    }

    @DisplayName("즐겨 찾기 생성")
    @Test
    void createFavoriteTest() {
        String source = "강남역";
        String target = "역삼역";
        Member member = new Member(1L, DummyTestUserInfo.EMAIL,DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        CreateFavoriteRequest createFavoriteRequest = new CreateFavoriteRequest(source, target);
        Station sourceStation = new Station(1L,source);
        Station targetStation = new Station(2L,target);
        given(favoriteRepository.save(any()))
                .willReturn(new Favorite(1L, sourceStation.getId(), targetStation.getId()));
        given(stationRepository.findByName("강남역")).willReturn(Optional.of(sourceStation));
        given(stationRepository.findByName("역삼역")).willReturn(Optional.of(targetStation));
        FavoriteResponse favorite = favoriteService.createFavorite(member, createFavoriteRequest);

        assertThat(favorite.getSource()).isEqualTo(source);
        assertThat(favorite.getTarget()).isEqualTo(target);
    }
}
