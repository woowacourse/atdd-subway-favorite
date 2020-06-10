package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";
    private static final String KANG_NAM_STATION_NAME = "강남역";
    private static final String JAM_SIL_STATION_NAME = "잠실역";
    private static final String DOGOK_STATION_NAME = "도곡역";

    private MemberService memberService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, stationRepository, jwtTokenProvider);
    }

    @DisplayName("회원가입 테스트")
    @Test
    void createMember() {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(memberRequest);

        verify(memberRepository).save(any());
    }

    @DisplayName("즐겨찾기 등록 테스트")
    @Test
    void addFavorite() {
        Station kangNamStation = new Station(KANG_NAM_STATION_NAME);
        Station jamSilStation = new Station(JAM_SIL_STATION_NAME);
        when(stationRepository.findByName(KANG_NAM_STATION_NAME)).thenReturn(Optional.of(kangNamStation));
        when(stationRepository.findByName(JAM_SIL_STATION_NAME)).thenReturn(Optional.of(jamSilStation));
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        FavoriteRequest favoriteRequest = new FavoriteRequest(KANG_NAM_STATION_NAME, JAM_SIL_STATION_NAME);

        memberService.addFavorite(member, favoriteRequest);

        verify(stationRepository).findByName(KANG_NAM_STATION_NAME);
        verify(stationRepository).findByName(JAM_SIL_STATION_NAME);
        verify(memberRepository).save(any());
    }

    @DisplayName("토큰을 생성하는 테스트")
    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("모든 즐겨찾기 조회")
    @Test
    void getFavorites() {
        Station kangNamStation = new Station(1L, KANG_NAM_STATION_NAME);
        Station jamSilStation = new Station(2L, JAM_SIL_STATION_NAME);
        Station dogukStation = new Station(3L, DOGOK_STATION_NAME);
        when(stationRepository.findByName(KANG_NAM_STATION_NAME)).thenReturn(Optional.of(kangNamStation));
        when(stationRepository.findByName(JAM_SIL_STATION_NAME)).thenReturn(Optional.of(jamSilStation));
        when(stationRepository.findByName(DOGOK_STATION_NAME)).thenReturn(Optional.of(dogukStation));

        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        List<Station> stations = Arrays.asList(kangNamStation, jamSilStation, dogukStation);
        when(stationRepository.findAll()).thenReturn(stations);

        FavoriteRequest favoriteRequest1 = new FavoriteRequest(KANG_NAM_STATION_NAME, JAM_SIL_STATION_NAME);
        FavoriteRequest favoriteRequest2 = new FavoriteRequest(KANG_NAM_STATION_NAME, DOGOK_STATION_NAME);
        memberService.addFavorite(member, favoriteRequest1);
        memberService.addFavorite(member, favoriteRequest2);

        Set<FavoriteResponse> favorites = memberService.findFavorites(member);

        assertThat(favorites.size()).isEqualTo(2);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        member.addFavorite(new Favorite(1L, 1L, 2L));
        member.addFavorite(new Favorite(2L, 1L, 3L));
        memberService.deleteFavorites(1L, member);

        assertThat(member.getFavorites().size()).isEqualTo(1);

        verify(memberRepository).save(member);
    }
}