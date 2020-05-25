package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";
    private static final String KANG_NAM_STATION_NAME = "강남역";
    private static final String JAM_SIL_STATION_NAME = "잠실역";

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

    @Test
    void createMember() {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(memberRequest);

        verify(memberRepository).save(any());
    }

    @Test
    void addFavorite() {
        Station kangNamStation = new Station(KANG_NAM_STATION_NAME);
        when(stationRepository.findByName(KANG_NAM_STATION_NAME)).thenReturn(Optional.of(kangNamStation));
        Station jamSilStation = new Station(JAM_SIL_STATION_NAME);
        when(stationRepository.findByName(JAM_SIL_STATION_NAME)).thenReturn(Optional.of(jamSilStation));
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        FavoriteRequest favoriteRequest = new FavoriteRequest(KANG_NAM_STATION_NAME, JAM_SIL_STATION_NAME);

        memberService.addFavorite(member, favoriteRequest);

        verify(stationRepository).findByName(KANG_NAM_STATION_NAME);
        verify(stationRepository).findByName(JAM_SIL_STATION_NAME);
        verify(memberRepository).save(any());
    }

    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }
}
