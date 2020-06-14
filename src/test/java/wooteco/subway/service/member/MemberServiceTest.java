package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberNotfoundException;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    private static final String TEST_USER_EMAIL = "brown@email.com";
    private static final String TEST_USER_NAME = "브라운";
    private static final String TEST_USER_PASSWORD = "brown";

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
        //given
        Member mockMember = mock(Member.class);
        UpdateMemberRequest request = mock(UpdateMemberRequest.class);
        String email = "testEmail";
        when(mockMember.getEmail()).thenReturn(email);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(mockMember));
        //when
        memberService.updateMember(mockMember, request);
        //then
        verify(memberRepository).save(mockMember);
    }

    @Test
    void updateMemberWhenNotPersistent() {
        String email = "test@test.com";
        Member member = mock(Member.class);
        when(member.getEmail()).thenReturn(email);
        UpdateMemberRequest request = mock(UpdateMemberRequest.class);
        when(memberRepository.findByEmail(email))
                .thenThrow(new MemberNotfoundException(MemberNotfoundException.INVALID_EMAIL_MESSAGE));

        assertThatThrownBy(() -> memberService.updateMember(member, request))
            .isInstanceOf(MemberNotfoundException.class)
            .hasMessage(MemberNotfoundException.INVALID_EMAIL_MESSAGE);
    }

    @Test
    void removeFavoritePathSuccessCase() {
        // given
        String email = "testEmail";
        Member mockMember = mock(Member.class);
        when(mockMember.getEmail()).thenReturn(email);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(mockMember));
        Station startStation = new Station("신정역");
        Station endStation = new Station("목동역");
        doNothing().when(mockMember).removeFavoritePath(startStation, endStation);

        // when
        memberService.removeFavoritePath(startStation, endStation, mockMember);

        // then
        verify(mockMember).removeFavoritePath(startStation, endStation);
    }

    @Test
    void removeFavoritePathFailCase() {
        // given
        String email = "testEmail";
        Member mockMember = mock(Member.class);
        when(mockMember.getEmail()).thenReturn(email);
        when(memberRepository.findByEmail(email))
                .thenThrow(new MemberNotfoundException(MemberNotfoundException.INVALID_EMAIL_MESSAGE));
        Station startStation = new Station("신정역");
        Station endStation = new Station("목동역");

        // when & then
        assertThatThrownBy(() -> memberService.removeFavoritePath(startStation, endStation, mockMember))
            .isInstanceOf(MemberNotfoundException.class)
            .hasMessage(MemberNotfoundException.INVALID_EMAIL_MESSAGE);
        verify(mockMember, times(0))
            .removeFavoritePath(startStation, endStation);
    }

    @Test
    void findFavoritePathsOf() {
        Set<FavoritePath> expected = Collections
            .singleton(new FavoritePath(new Station("신정역"), new Station("잠실역")));
        Member mockMember = mock(Member.class);
        when(mockMember.getFavoritePaths()).thenReturn(expected);

        List<FavoritePath> favoritePaths = memberService.findFavoritePathsOf(mockMember);

        assertThat(favoritePaths).isEqualTo(Collections.unmodifiableList(new ArrayList<>(expected)));
    }
}
