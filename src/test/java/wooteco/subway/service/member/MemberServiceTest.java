package wooteco.subway.service.member;

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
import wooteco.subway.service.member.dto.UpdateMemberRequest;

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

    @DisplayName("회원 가입 테스트")
    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @DisplayName("로그인 테스트")
    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("로그인 후, 자신의 정보 조회")
    @Test
    void findMemberByEmail() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(member));

        memberService.findMemberByEmail(TEST_USER_EMAIL);

        verify(memberRepository).findByEmail(TEST_USER_EMAIL);
    }

    @DisplayName("로그인 후, 회원 정보 수정 테스트")
    @Test
    void updateMemberByUser() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest(null, null);

        when(memberRepository.save(member)).thenReturn(member);

        memberService.updateMemberByUser(member, updateMemberRequest);

        verify(memberRepository).save(member);
    }

    @DisplayName("로그인 후, 회원 정보 삭제 테스트")
    @Test
    void deleteMemberByUser() {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        doNothing().when(memberRepository).deleteById(1L);

        memberService.deleteMemberByUser(member);

        verify(memberRepository).deleteById(1L);
    }
}
