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
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";
    public static final String TEST_USER_NAME2 = "터틀";
    public static final String TEST_USER_PASSWORD2 = "turtle";
    public static final long TEST_USER_ID = 1L;

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
    }

    @DisplayName("회원 생성 테스트")
    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @DisplayName("토큰 생성 테스트")
    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("회원 정보 조회 테스트")
    @Test
    void getMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        Member memberFound = memberService.findMemberByEmail(TEST_USER_EMAIL);
        assertThat(memberFound).isEqualTo(member);
    }

    @DisplayName("회원 정보 수정 테스트")
    @Test
    void updateMember() {
        // given
        Member member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        // when
        memberService.updateMember(member.getId(), new UpdateMemberRequest(TEST_USER_NAME2, TEST_USER_PASSWORD2));

        // then
        assertThat(member.getName()).isEqualTo(TEST_USER_NAME2);
        assertThat(member.getPassword()).isEqualTo(TEST_USER_PASSWORD2);
        verify(memberRepository).save(member);
    }

    @DisplayName("회원 정보 삭제 테스트")
    @Test
    void deleteMember() {
        // given
        Member member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        // when
        memberService.deleteMember(member.getId());

        // then
        verify(memberRepository).deleteById(member.getId());
    }
    // TODO: 2020/05/20 컨트롤러에서의 인증 방식 고민해보기
    // TODO: 2020/05/20 Exception Handler 추가
    // TODO: 2020/05/21 MockMvc 리팩토링
    // TODO: 2020/05/20 Dynamic Test 방식으로 변경
    // TODO: 2020/05/20 페이지 연동
}
