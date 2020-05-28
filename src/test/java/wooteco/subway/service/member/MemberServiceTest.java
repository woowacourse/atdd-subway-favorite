package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.infra.TokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";
    private static final long TEST_USER_ID = 1L;
    private static final String TEST_USER_NEW_NAME = "귀한분";
    private static final String TEST_USER_NEW_PASSWORD = "666";

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, tokenProvider);
    }

    @DisplayName("회원 가입시 repository의 save 메서드를 정상 호출한다.")
    @Test
    void createMember() {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        when(memberRepository.save(any())).thenReturn(member);
        memberService.createMember(memberRequest);

        verify(memberRepository).save(any());
    }

    @DisplayName("중복된 이메일로 회원가입 하는 경우, DuplicateEmailException을 던진다.")
    @Test
    void createMemberWithDuplicateEmail() {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        when(memberRepository.existsByEmail(any())).thenReturn(true);
        assertThatThrownBy(() -> memberService.createMember(memberRequest))
            .isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("로그인시 Jwt 토큰 발급 기능 수행시,  jwtTokenProvider의 토큰 생성 기능을 정상 호출한다.")
    @Test
    void createJwtToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createJwtToken(loginRequest);

        verify(tokenProvider).createToken(anyString());
    }

    @DisplayName("회원정보 업데이트시, 요청한 정보대로 회원의 정보가 변경된다.")
    @Test
    void updateMember() {
        Member member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        Member newMember = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NEW_NAME, TEST_USER_NEW_PASSWORD);

        memberService.updateMember(TEST_USER_ID, new UpdateMemberRequest(TEST_USER_NEW_NAME, TEST_USER_NEW_PASSWORD));

        assertThat(member).isEqualTo(newMember);
        verify(memberRepository).save(any());
    }

    @DisplayName("회원정보 삭제시, memberRepository의 deleteById를 정상 호출한다.")
    @Test
    void deleteMember() {
        when(memberRepository.existsById(any())).thenReturn(true);
        memberService.deleteMember(1L);

        verify(memberRepository).deleteById(any());
    }

    @DisplayName("존재하지 않는 회원정보 삭제시, EntityNotFoundException 예외를 던진다.")
    @Test
    void deleteMemberNotExistingMemberId() {
        when(memberRepository.existsById(any())).thenReturn(false);
        assertThatThrownBy(() -> memberService.deleteMember(1L))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("이메일을 통한 회원 정보 조회시, memberRepository의 findByEmail을 정상 호출한다.")
    @Test
    void findMemberByEmail() {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        memberService.findMemberByEmail(TEST_USER_EMAIL);

        verify(memberRepository).findByEmail(any());
    }

    @DisplayName("존재하지 않는 이메일을 통한 회원 정보 조회시, memberRepository의 findByEmail을 정상 호출한다.")
    @Test
    void findMemberByEmailWhenMemberNotExistWithInputEmail() {
        when(memberRepository.findByEmail(any())).thenThrow(new EntityNotFoundException("해당하는 이메일이 없습니다."));
        assertThatThrownBy(() -> memberService.findMemberByEmail(TEST_USER_EMAIL))
            .isInstanceOf(EntityNotFoundException.class);

        verify(memberRepository).findByEmail(any());
    }

    @DisplayName("폼을 통한 로그인 시도시, memberRepository의 findByEmail을 정상 호출한다.")
    @ParameterizedTest
    @CsvSource(value = {"brown, true", "cu, false"})
    void loginWithForm(String password, boolean expected) {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, password);
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        boolean actual = memberService.loginWithForm(loginRequest);

        verify(memberRepository).findByEmail(anyString());
        assertThat(actual).isEqualTo(expected);
    }
}
