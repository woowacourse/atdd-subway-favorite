package woowa.bossdog.subway.service.favorite;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import woowa.bossdog.subway.domain.Favorite;
import woowa.bossdog.subway.domain.Member;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.repository.FavoriteRepository;
import woowa.bossdog.subway.repository.StationRepository;
import woowa.bossdog.subway.service.favorite.dto.FavoriteRequest;
import woowa.bossdog.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
class FavoriteServiceTest {

    private FavoriteService favoriteService;

    @Mock private FavoriteRepository favoriteRepository;
    @Mock private StationRepository stationRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        member = new Member(111L, "test@test.com", "bossdog", "test");
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void createFavorite() {
        // given
        FavoriteRequest request = new FavoriteRequest(3L, 4L);
        Station sourceStation = new Station(3L, "강남역");
        Station targetStation = new Station(4L, "서울역");
        Favorite favorite = new Favorite(10L, member, sourceStation, targetStation);

        given(stationRepository.findById(eq(3L))).willReturn(Optional.of(sourceStation));
        given(stationRepository.findById(eq(4L))).willReturn(Optional.of(targetStation));

        // when
        FavoriteResponse response = favoriteService.createFavorite(member, request);

        // then
        assertThat(response.getMemberId()).isEqualTo(favorite.getMember().getId());
        assertThat(response.getSource().getName()).isEqualTo(favorite.getSourceStation().getName());
        assertThat(response.getTarget().getName()).isEqualTo(favorite.getTargetStation().getName());
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void listFavorite() {
        // given
        List<Favorite> favorites = Lists.newArrayList(
                new Favorite(10L, member, new Station(3L, "강남역"), new Station(4L, "서울역")),
                new Favorite(11L, member, new Station(8L, "신촌역"), new Station(21L, "사당역"))
        );
        given(favoriteRepository.findAllByMemberId(any())).willReturn(favorites);

        // when
        List<FavoriteResponse> responses = favoriteService.listFavorites(member);

        // then
        verify(favoriteRepository).findAllByMemberId(eq(111L));
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getSource().getName()).isEqualTo("강남역");
        assertThat(responses.get(0).getTarget().getName()).isEqualTo("서울역");
        assertThat(responses.get(1).getSource().getName()).isEqualTo("신촌역");
        assertThat(responses.get(1).getTarget().getName()).isEqualTo("사당역");
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        Member member = new Member(111L, "test@test.com", "bossdog", "test");

        // when
        favoriteService.deleteFavorite(member, 3L);

        // then
        verify(favoriteRepository).deleteById(eq(3L));
    }

}