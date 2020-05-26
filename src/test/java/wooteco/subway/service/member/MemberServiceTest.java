package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Sql("/truncate.sql")
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "bossdog@email.com";
    public static final String TEST_USER_NAME = "boss";
    public static final String TEST_USER_PASSWORD = "dog";

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

    @DisplayName("회원 가입")
    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @DisplayName("로그인")
    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("회원 정보 수정")
    @Test
    void updateMember() {
        Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        final UpdateMemberRequest updateParam = new UpdateMemberRequest("수정된 보스독", "수정된 비밀번호");
        memberService.updateMember(member.getId(), updateParam);

        verify(memberRepository).findById(eq(member.getId()));
    }

    @DisplayName("회원 탈퇴 기능")
    @Test
    void deleteMember() {
        memberService.deleteMember(63L);
        verify(memberRepository).deleteById(eq(63L));
    }

    @DisplayName("회원 탈퇴 시 관련 즐겨찾기도 모두 삭제")
    @Test
    void deleteMemberWithFavorites() {
        memberService.deleteMember(63L);
        verify(favoriteRepository).deleteAllByMemberId(eq(63L));
    }
}
