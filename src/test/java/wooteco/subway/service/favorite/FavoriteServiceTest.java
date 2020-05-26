package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    StationRepository stationRepository;

    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository);
    }

    @DisplayName("회원의 즐겨찾기 노선 목록 전체를 조회")
    @Test
    void getFavorites() {
        Set<Favorite> favorites = new HashSet<>(Arrays.asList(new Favorite(1L, 2L), new Favorite(2L, 3L)));
        Member member = new Member(1L, "sample@sample", "sample", "sample", new Favorites(favorites));

        Station gangnam = new Station(1L, "강남");
        Station gangbuk = new Station(2L, "강북");
        Station gangdong = new Station(3L, "강동");
        List<Station> stations = Arrays.asList(gangnam, gangbuk, gangdong);
        when(stationRepository.findAllById(anySet())).thenReturn(stations);
        List<FavoriteResponse> favorites1 = favoriteService.getFavorites(member);

        List<FavoriteResponse> expected = Arrays.asList(
            new FavoriteResponse(StationResponse.of(gangnam), StationResponse.of(gangbuk)),
            new FavoriteResponse(StationResponse.of(gangbuk), StationResponse.of(gangdong)));

        assertThat(favorites1).isEqualTo(expected);
    }
}