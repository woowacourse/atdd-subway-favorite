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

    private Station jamsil;
    private Station gangnam;

    @BeforeEach
    private void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);
        jamsil = new Station(1L, "잠실");
        gangnam = new Station(2L, "강남");
    }

    @Test
    void createWhenValidInput() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(jamsil.getId(), gangnam.getId());
        Favorite favorite = new Favorite(1L, 1L, favoriteRequest.getDepartureId(),
                favoriteRequest.getArrivalId());

        when(stationRepository.findById(favoriteRequest.getDepartureId()))
                .thenReturn(Optional.of(new Station("잠실")));
        when(stationRepository.findById(favoriteRequest.getArrivalId()))
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
        FavoriteRequest duplicatedFavoriteRequest = new FavoriteRequest(jamsil.getId(), gangnam.getId());

        when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member("a@a", "a", "a")));
        when(stationRepository.findById(duplicatedFavoriteRequest.getDepartureId()))
                .thenReturn(Optional.of(new Station(1L, "잠실")));
        when(stationRepository.findById(duplicatedFavoriteRequest.getArrivalId()))
                .thenReturn(Optional.of(new Station(2L, "강남")));
        when(favoriteRepository.findAllByMemberId(duplicatedMemberId))
                .thenReturn(Collections.singletonList(new Favorite(duplicatedMemberId,
                        duplicatedFavoriteRequest.getDepartureId(), duplicatedFavoriteRequest.getArrivalId())));

        assertThatThrownBy(() -> favoriteService.create(duplicatedMemberId, duplicatedFavoriteRequest))
                .isInstanceOf(DuplicateFavoriteException.class);
    }

    @Test
    void deleteTest() {
        Long memberId = 1L;
        FavoriteRequest favoriteRequest = new FavoriteRequest(jamsil.getId(), gangnam.getId());

        when(favoriteRepository.findByMemberIdAndDepartureIdAndArrivalId(memberId,
                favoriteRequest.getDepartureId(), favoriteRequest.getArrivalId()))
                .thenReturn(Optional.of(
                        new Favorite(memberId, favoriteRequest.getDepartureId(), favoriteRequest.getArrivalId())));

        favoriteService.delete(memberId, favoriteRequest);

        verify(favoriteRepository).delete(any());
    }
}