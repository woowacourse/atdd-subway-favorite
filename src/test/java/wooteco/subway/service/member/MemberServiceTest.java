package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
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

    @Test
    void updateMemberSuccessCase() {
        Member member = mock(Member.class);
        when(member.isNotPersistent()).thenReturn(false);

        memberService.updateMember(member);

        verify(memberRepository).save(member);
    }

    @Test
    void updateMemberFailCase() {
        Member member = mock(Member.class);
        when(member.isNotPersistent()).thenReturn(true);

        assertThatThrownBy(() -> memberService.updateMember(member))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MemberService.NOT_MANAGED_BY_REPOSITORY);
    }

    @Test
    void removeFavoritePathSuccessCase() {
        // given
        Member mockMember = mock(Member.class);
        Station startStation = new Station("신정역");
        Station endStation = new Station("목동역");
        when(mockMember.isNotPersistent()).thenReturn(false);
        doNothing().when(mockMember).removeFavoritePath(startStation, endStation);

        // when
        memberService.removeFavoritePath(startStation, endStation, mockMember);

        // then
        verify(mockMember).removeFavoritePath(startStation, endStation);
    }

    @Test
    void removeFavoritePathFailCase() {
        // given
        Member mockMember = mock(Member.class);
        Station startStation = new Station("신정역");
        Station endStation = new Station("목동역");
        when(mockMember.isNotPersistent()).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.removeFavoritePath(startStation, endStation, mockMember))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MemberService.NOT_MANAGED_BY_REPOSITORY);
        verify(mockMember, times(0))
            .removeFavoritePath(startStation, endStation);
    }
}
