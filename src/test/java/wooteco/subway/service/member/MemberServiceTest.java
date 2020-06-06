package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.InvalidAuthenticationException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.exception.DuplicateMemberException;
import wooteco.subway.exception.NoSuchMemberException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, favoriteRepository, jwtTokenProvider);
    }

    @DisplayName("멤버 생성 테스트")
    @Test
    void createMemberTest() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @DisplayName("멤버 생성시, 이미 존재하는 이메일일때 예외가 발생하는지 테스트")
    @Test
    void memberDuplicateTest() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(new Member()));
        assertThatThrownBy(() -> memberService.createMember(member))
                .isInstanceOf(DuplicateMemberException.class);
    }


    @DisplayName("멤버 업데이트 테스트")
    @Test
    void updateMemberTest() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        UpdateMemberRequest request = new UpdateMemberRequest("NEW브라운", "NEWbrown");
        memberService.updateMemberById(1L, request);

        verify(memberRepository).save(any());
    }

    @DisplayName("멤버 삭제 테스트")
    @Test
    void deleteMemberTest() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        memberService.deleteMemberById(1L);

        verify(memberRepository).deleteById(any());
    }

    @DisplayName("멤버 삭제시, 즐겨찾기 목록도 같이 삭제되는지 테스트")
    @Test
    void deleteMemberTest2() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        doNothing().when(memberRepository).deleteById(any());

        memberService.deleteMember(member);

        verify(favoriteRepository).deleteAllByMemberId(any());
    }

    @DisplayName("멤버 삭제 시, 삭제하고자 하는 멤버를 찾을 수 없을 때 예외가 발생하는지 테스트")
    @Test
    void cannotFoundDeleteMemberTest() {
        when(memberRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.deleteMemberById(1L))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessage("해당하는 멤버를 찾을 수 없습니다.");

    }

    @DisplayName("로그인했을때 토큰을 잘 생성하는지 테스트")
    @Test
    void createTokenTest() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("잘못된 비밀번호로 요청이 온 경우 예외가 발생하는지 테스트")
    @Test
    void invalidPasswordTest() {
        String wrongPassword = "1234";
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD)));

        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, wrongPassword);

        assertThatThrownBy(() -> memberService.createToken(loginRequest))
                .isInstanceOf(InvalidAuthenticationException.class)
                .hasMessage("잘못된 패스워드");
    }
}
