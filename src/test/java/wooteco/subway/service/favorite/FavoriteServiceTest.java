package wooteco.subway.service.favorite;

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

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void createWhenDuplicatedInput() {
        Long duplicatedMemberId = 1L;
        FavoriteRequest duplicatedFavoriteRequest = new FavoriteRequest("잠실", "강남");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member("a@a", "a", "a")));
        when(stationRepository.findByName(duplicatedFavoriteRequest.getDeparture()))
                .thenReturn(Optional.of(new Station(duplicatedFavoriteRequest.getDeparture())));
        when(stationRepository.findByName(duplicatedFavoriteRequest.getArrival()))
                .thenReturn(Optional.of(new Station(duplicatedFavoriteRequest.getArrival())));
        when(favoriteRepository.findAllByMemberId(duplicatedMemberId))
                .thenReturn(Collections.singletonList(new Favorite(duplicatedMemberId,
                        duplicatedFavoriteRequest.getDeparture(), duplicatedFavoriteRequest.getArrival())));

        assertThatThrownBy(() -> favoriteService.create(duplicatedMemberId, duplicatedFavoriteRequest))
                .isInstanceOf(DuplicateFavoriteException.class);
    }

    @Test
    void deleteTest() {
        Long memberId = 1L;
        FavoriteRequest favoriteRequest = new FavoriteRequest("잠실", "강남");

        when(favoriteRepository.findByMemberIdAndDepartureAndArrival(memberId,
                favoriteRequest.getDeparture(), favoriteRequest.getArrival()))
                .thenReturn(Optional.of(
                        new Favorite(memberId, favoriteRequest.getDeparture(), favoriteRequest.getArrival())));

        favoriteService.delete(memberId, favoriteRequest);

        verify(favoriteRepository).delete(any());
    }
}