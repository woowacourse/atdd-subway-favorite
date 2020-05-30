package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

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
import wooteco.subway.service.favorite.exception.DuplicateFavoriteException;

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
        Favorite favorite = new Favorite(1L, 1L, 1L,
                2L);

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

    @Test
    void createWhenDuplicatedInput() {
        Long duplicatedMemberId = 1L;
        FavoriteRequest duplicatedFavoriteRequest = new FavoriteRequest("잠실", "강남");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member("a@a", "a", "a")));
        when(stationRepository.findByName(duplicatedFavoriteRequest.getDeparture()))
                .thenReturn(Optional.of(new Station(1L, duplicatedFavoriteRequest.getDeparture())));
        when(stationRepository.findByName(duplicatedFavoriteRequest.getArrival()))
                .thenReturn(Optional.of(new Station(2L, duplicatedFavoriteRequest.getArrival())));
        when(favoriteRepository.findAllByMemberId(duplicatedMemberId))
                .thenReturn(Collections.singletonList(new Favorite(duplicatedMemberId,
                        1L, 2L)));

        assertThatThrownBy(() -> favoriteService.create(duplicatedMemberId, duplicatedFavoriteRequest))
                .isInstanceOf(DuplicateFavoriteException.class);
    }

    @Test
    void deleteTest() {
        Long memberId = 1L;
        String jamSil = "잠실";
        String gangNam = "강남";
        FavoriteRequest favoriteRequest = new FavoriteRequest(jamSil, gangNam);

        when(stationRepository.findByName(jamSil)).thenReturn(Optional.of(new Station(1L, jamSil)));
        when(stationRepository.findByName(gangNam)).thenReturn(Optional.of(new Station(2L, gangNam)));

        when(favoriteRepository.findByMemberIdAndDepartureStationIdAndArrivalStationId(memberId,
                1L, 2L))
                .thenReturn(Optional.of(
                        new Favorite(memberId, 1L, 2L)));

        favoriteService.delete(memberId, favoriteRequest);

        verify(favoriteRepository).delete(any());
    }
}

