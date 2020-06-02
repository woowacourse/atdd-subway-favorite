package wooteco.subway.service.favorite;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static wooteco.subway.AcceptanceTest.TEST_USER_EMAIL;
import static wooteco.subway.AcceptanceTest.TEST_USER_PASSWORD;
import static wooteco.subway.service.constants.ErrorMessage.ALREADY_EXIST_FAVORITE;
import static wooteco.subway.service.constants.ErrorMessage.NOT_EXIST_FAVORITE;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    FavoriteService favoriteService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void create() {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1L, "강남역"));
        stations.add(new Station(2L, "잠실역"));
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);

        when(stationRepository.findAll()).thenReturn(stations);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest("강남역",
                "잠실역");
        favoriteService.create(member, favoriteCreateRequest);

        verify(memberRepository).save(member);
    }

    @DisplayName("즐겨찾기 추가 - 이미 존재하는 즐겨찾기")
    @Test
    void create_IfAlreadyExist_ThrowException() {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1L, "강남역"));
        stations.add(new Station(2L, "잠실역"));
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);

        when(stationRepository.findAll()).thenReturn(stations);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest("강남역",
                "잠실역");
        favoriteService.create(member, favoriteCreateRequest);
        assertThatThrownBy(() -> favoriteService.create(member, favoriteCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ALREADY_EXIST_FAVORITE);
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void find() {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1L, "강남역"));
        stations.add(new Station(2L, "잠실역"));
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);
        member.addFavorite(new Favorite(1L, 2L));

        List<FavoriteResponse> expect = Lists.newArrayList(
                new FavoriteResponse("강남역", "잠실역"));

        when(stationRepository.findAll()).thenReturn(stations);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        List<FavoriteResponse> favoriteResponses = favoriteService.find(member);

        assertThat(favoriteResponses).isEqualTo(expect);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void delete() {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1L, "강남역"));
        stations.add(new Station(2L, "잠실역"));
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);
        member.addFavorite(new Favorite(1L, 2L));

        when(stationRepository.findAll()).thenReturn(stations);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest("강남역",
                "잠실역");
        favoriteService.delete(member, favoriteDeleteRequest);

        verify(memberRepository).save(any());
    }

    @DisplayName("즐겨찾기 삭제 - 존재하지 않는 즐겨찾기")
    @Test
    void delete_IfNotExist_ThrowException() {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1L, "강남역"));
        stations.add(new Station(2L, "잠실역"));
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);
        member.addFavorite(new Favorite(1L, 2L));

        when(stationRepository.findAll()).thenReturn(stations);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest("강남역",
                "잠실역");
        favoriteService.delete(member, favoriteDeleteRequest);

        assertThatThrownBy(() -> favoriteService.delete(member, favoriteDeleteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXIST_FAVORITE);
    }
}