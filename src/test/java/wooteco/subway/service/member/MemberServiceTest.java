package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

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
import wooteco.subway.service.member.exception.DuplicateMemberException;
import wooteco.subway.service.member.exception.IncorrectPasswordException;
import wooteco.subway.service.member.exception.NotFoundMemberException;

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
    @DisplayName("회원 가입 성공")
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("회원 가입 - 이미 존재하는 사용자가 있으면 예외 발생")
    void createMember_throw_exception_when_duplicate_member_exist() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.save(any())).thenThrow(DuplicateMemberException.class);

        assertThatExceptionOfType(DuplicateMemberException.class)
            .isThrownBy(() -> memberService.createMember(member));
    }

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @Test
    @DisplayName("토큰 생성 - 사용자가 존재하지 않는 경우 예외 발생")
    void createToken_throw_exception_when_member_not_exist() {
        when(memberRepository.findByEmail(anyString())).thenThrow(NotFoundMemberException.class);
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        assertThatExceptionOfType(NotFoundMemberException.class)
            .isThrownBy(() -> memberService.createToken(loginRequest));
    }

    @Test
    @DisplayName("토큰 생성 - 비밀번호가 일치하지 않는 경우")
    void createToken_throw_exception_when_incorrect_password() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, "incorrect");

        assertThatExceptionOfType(IncorrectPasswordException.class)
            .isThrownBy(() -> memberService.createToken(loginRequest));
    }
}
