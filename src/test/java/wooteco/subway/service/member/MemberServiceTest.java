package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exceptions.DuplicatedEmailException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @DisplayName("회원 생성을 정상적으로 하는지 확인")
    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @DisplayName("로그인 시 토큰 생성을 하는지 확인")
    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("이미 가입된 이메일로 회원 생성 시 실패하는지 확인")
    @Test
    void failToCreateMemberIfAlreadyRegisteredMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.createMember(member))
                .isInstanceOf(DuplicatedEmailException.class)
                .hasMessage(String.format("%s 로 가입한 회원이 존재합니다.", TEST_USER_EMAIL));
    }

    @DisplayName("회원정보 업데이트 시 성공하는지 확인")
    @Test
    void updateMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Member expectedMember = new Member(TEST_USER_EMAIL, "update " + TEST_USER_NAME, TEST_USER_PASSWORD);
        UpdateMemberRequest request = new UpdateMemberRequest("update " + TEST_USER_NAME, TEST_USER_PASSWORD,
                                                              TEST_USER_PASSWORD);
        memberService.updateMember(member, request);

        verify(memberRepository).save(expectedMember);
    }

    @DisplayName("회원정보 삭제 시 성공하는지 확인")
    @Test
    void deleteMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        memberService.deleteMember(member);

        verify(memberRepository).delete(member);
    }
}
