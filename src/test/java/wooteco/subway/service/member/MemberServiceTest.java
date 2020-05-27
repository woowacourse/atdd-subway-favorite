package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
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
        given(memberRepository.save(any())).willThrow(DuplicateMemberException.class);

        assertThatExceptionOfType(DuplicateMemberException.class)
                .isThrownBy(() -> memberService.createMember(member));
    }

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @Test
    @DisplayName("토큰 생성 - 사용자가 존재하지 않는 경우 예외 발생")
    void createToken_throw_exception_when_member_not_exist() {
        given(memberRepository.findByEmail(anyString())).willThrow(NotFoundMemberException.class);
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        assertThatExceptionOfType(NotFoundMemberException.class)
                .isThrownBy(() -> memberService.createToken(loginRequest));
    }

    @Test
    @DisplayName("토큰 생성 - 비밀번호가 일치하지 않는 경우")
    void createToken_throw_exception_when_incorrect_password() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, "incorrect");

        assertThatExceptionOfType(IncorrectPasswordException.class)
                .isThrownBy(() -> memberService.createToken(loginRequest));
    }

    @Test
    @DisplayName("이메일로 회원 조회")
    void findMemberByEmail() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        assertThat(memberService.findMemberByEmail(TEST_USER_EMAIL)).isEqualTo(member);
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 시 예외 발생")
    void findMemberByInvalidEmail() {
        given(memberRepository.findByEmail("invalid_email")).willReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundMemberException.class)
                .isThrownBy(() -> memberService.findMemberByEmail("invalid_email"));
    }

    @Test
    @DisplayName("회원 정보 수정")
    void updateMember() {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("updateName",
                "updatePassword");
        memberService.updateMember(member.getId(), updateMemberRequest);

        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMember() {
        memberService.deleteMember(1L);
        verify(memberRepository).deleteById(any());
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void addFavorite() {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Favorite favorite = new Favorite(1L, 2L);
        memberService.addFavorite(member, favorite);
        verify(memberRepository).save(any());
        assertThat(member.hasFavorite(favorite)).isTrue();
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    void getFavorites() {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        memberService.getFavorites(member);
        verify(memberRepository).findFavoritesById(any());
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void removeFavorite() {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Favorite favorite = new Favorite(1L, 2L);
        member.addFavorite(favorite);
        memberService.removeFavorite(member, 1L, 2L);
        verify(memberRepository).save(any());
        assertThat(member.hasFavorite(favorite)).isFalse();
    }

    @Test
    @DisplayName("즐겨찾기 여부 확인")
    void hasFavorite() {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(memberService.hasFavorite(member, 1L, 2L)).isFalse();
    }
}
