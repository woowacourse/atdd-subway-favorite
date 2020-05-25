package wooteco.subway.service.favorite;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    private FavoriteService favoriteService;

    @BeforeEach
    private void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);
    }

    @Test
    void createWhenValidInput() {
        FavoriteRequest favoriteRequest = new FavoriteRequest("잠실", "강남");
        Favorite favorite = new Favorite(1L, 1L, favoriteRequest.getDeparture(),
                favoriteRequest.getArrival());
        when(stationRepository.findByName(favoriteRequest.getDeparture()))
                .thenReturn(Optional.of(new Station("잠실")));
        when(stationRepository.findByName(favoriteRequest.getArrival()))
                .thenReturn(Optional.of(new Station("강남")));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member()));
        when(favoriteRepository.findAllByMemberId(1L))
                .thenReturn(Collections.emptyList());
        when(favoriteRepository.save(any())).thenReturn(favorite);

        favoriteService.create(1L, favoriteRequest);

        verify(favoriteRepository).save(any());
    }
}