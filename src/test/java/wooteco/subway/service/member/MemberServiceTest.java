package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static wooteco.subway.service.constants.ErrorMessage.ALREADY_EXIST_MEMBER;
import static wooteco.subway.service.constants.ErrorMessage.NOT_EXIST_MEMBER;

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

    @DisplayName("회원가입")
    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @DisplayName("회원가입 - 이미 존재하는 회원")
    @Test
    void createMember_IfAlreadyExist_ThrowException() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        assertThatThrownBy(() -> memberService.createMember(member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ALREADY_EXIST_MEMBER);
    }

    @DisplayName("로그인 토큰 생성")
    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("Email로 회원 검색")
    @Test
    void findMemberByEmail() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(
                Optional.of(member));

        Member memberByEmail = memberService.findMemberByEmail(TEST_USER_EMAIL);

        assertThat(memberByEmail).isEqualTo(member);
    }

    @DisplayName("Email로 회원 검색 - 존재하지 않는 회원")
    @Test
    void findMemberByEmail_IfNotExistMember_ThrowException() {
        when(memberRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findMemberByEmail(TEST_USER_EMAIL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXIST_MEMBER);
    }
}
