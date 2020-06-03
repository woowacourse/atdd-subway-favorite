package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.station.StationService;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";
    public static final String TEST_STATION_NAME_GANGNAM = "강남역";
    public static final String TEST_STATION_NAME_JAMSIL = "잠실역";
    public static final String TEST_STATION_NAME_SEOUL = "서울역";

    private FavoriteService favoriteService;
    private Member member;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private StationService stationService;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(memberRepository, stationService);

        member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        member.addFavorite(new Favorite(1L, 2L));
    }

    @Test
    void createFavorite() {
        when(stationService.findStationByName(TEST_STATION_NAME_GANGNAM)).thenReturn(new Station(1L, TEST_STATION_NAME_GANGNAM));
        when(stationService.findStationByName(TEST_STATION_NAME_SEOUL)).thenReturn(new Station(3L, TEST_STATION_NAME_SEOUL));

        favoriteService.createFavorite(new FavoriteRequest(TEST_STATION_NAME_GANGNAM, TEST_STATION_NAME_SEOUL), member);

        verify(memberRepository).save(any());
    }

    @Test
    void removeFavorite() {
        when(stationService.findStationByName(TEST_STATION_NAME_GANGNAM)).thenReturn(new Station(1L, TEST_STATION_NAME_GANGNAM));
        when(stationService.findStationByName(TEST_STATION_NAME_JAMSIL)).thenReturn(new Station(2L, TEST_STATION_NAME_JAMSIL));

        favoriteService.removeFavorite(new FavoriteRequest(TEST_STATION_NAME_GANGNAM, TEST_STATION_NAME_JAMSIL), member);

        verify(memberRepository).save(any());

        assertThat(member.getFavorites().size()).isEqualTo(0);
    }

    @Test
    void existFavorite() {
        when(stationService.findStationByName(TEST_STATION_NAME_GANGNAM)).thenReturn(new Station(1L, TEST_STATION_NAME_GANGNAM));
        when(stationService.findStationByName(TEST_STATION_NAME_JAMSIL)).thenReturn(new Station(2L, TEST_STATION_NAME_JAMSIL));

        assertTrue(favoriteService.existsFavorite(new FavoriteRequest(TEST_STATION_NAME_GANGNAM, TEST_STATION_NAME_JAMSIL), member));
    }
}
