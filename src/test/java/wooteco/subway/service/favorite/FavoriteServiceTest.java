package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

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
        String sourceName = "강남역";
        String targetName = "신촌역";
        Long sourceId = 1L;
        Long targetId = 2L;
        Long memberId = 1L;
        given(favoriteRepository.save(any())).willReturn(new Favorite(memberId, sourceId, targetId));
        Member member = new Member(1L, "slowbro@gmail.com", "slowbro", "1234");

        given(stationRepository.findByName(sourceName)).willReturn(Optional.of(new Station(1L, sourceName)));
        given(stationRepository.findByName(targetName)).willReturn(Optional.of(new Station(2L, targetName)));

        CreateFavoriteRequest createFavoriteRequest = new CreateFavoriteRequest(sourceName, targetName);
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(member, createFavoriteRequest);


        assertThat(favoriteResponse.getSource()).isEqualTo(sourceName);
        assertThat(favoriteResponse.getTarget()).isEqualTo(targetName);
    }
}
