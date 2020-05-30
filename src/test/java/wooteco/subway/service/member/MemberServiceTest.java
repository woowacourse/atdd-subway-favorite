package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.station.StationService;
import wooteco.subway.web.member.InvalidRegisterException;
import wooteco.subway.web.member.InvalidUpdateException;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";
    public static final String TEST_JASON_EMAIL = "jason@email.com";
    public static final String TEST_JASON_NAME = "제이슨";
    public static final String TEST_JASON_PASSWORD = "jason";
    public static final String TEST_UPDATE_USER_NAME = "브라운";
    public static final String TEST_UPDATE_USER_PASSWORD = "brown";
    public static final String STATION_NAME_DOGOK = "도곡";
    public static final String STATION_NAME_GANGNAM = "강남";

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private StationService stationService;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, favoriteRepository, jwtTokenProvider, stationService);
    }

    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

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

    @DisplayName("이메일이 중복되는 경우 예외 처리")
    @Test
    void createTokenEmailDuplicate() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        lenient().when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.createMember(member))
                .isInstanceOf(InvalidRegisterException.class)
                .hasMessage(InvalidRegisterException.DUPLICATE_EMAIL_MSG);
    }

    @Test
    void updateOtherAccount() {
        Member member1 = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Member member2 = new Member(TEST_JASON_EMAIL, TEST_JASON_NAME, TEST_JASON_PASSWORD);

        UpdateMemberRequest updateData = new UpdateMemberRequest(TEST_UPDATE_USER_NAME, TEST_UPDATE_USER_PASSWORD);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member1));
        lenient().when(jwtTokenProvider.getSubject(any())).thenReturn(member2.getEmail());

        assertThatThrownBy(() -> memberService.updateMember(member2, member1.getId(), updateData))
                .isInstanceOf(InvalidUpdateException.class);
    }

    @Test
    void findAllFavorites() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(member.getId(), 1L, 3L);

        memberService.addFavorite(member, favoriteCreateRequest);

        when(stationService.findStations()).thenReturn(Arrays.asList(new Station(1L, STATION_NAME_GANGNAM), new Station(3L, STATION_NAME_DOGOK)));

        assertThat(memberService.findAllFavorites(member)).isNotNull();
        assertThat(memberService.findAllFavorites(member).size()).isEqualTo(1);
    }
}
